-- UUID Unification: single migration to ensure sequential execution
-- Fully idempotent: safe to run on a DB that already has some/all changes applied
--
-- Context:
--   mission_action has: mission_id (INT), owner_id (UUID) — owner_id is the single parent FK
--   mission_general_info/crew/passenger have: mission_id (INT), mission_id_uuid (UUID)
--   Local `mission` rows are created lazily at runtime from MonitorEnv; `mission.external_id`
--   (the old MonitorEnv Int id) is added here and must be populated BEFORE the Int columns are
--   dropped, otherwise the Int -> UUID mapping in Phase 2 orphans every not-yet-linked child row.
--
DO $$
DECLARE
    v_linked   bigint;
    v_created  bigint;
    v_missing  bigint;
BEGIN

    -- ==========================================
    -- Phase 1: Prepare the mission table
    -- ==========================================

    -- service_id becomes nullable: env-mirror / synthetic rows have no local service.
    -- start_datetime_utc intentionally KEPT NOT NULL — MissionModel.startDateTimeUtc is non-null
    -- and every insert (runtime and synthetic below) provides a value; re-asserted after backfill.
    ALTER TABLE mission ALTER COLUMN service_id DROP NOT NULL;

    ALTER TABLE mission ADD COLUMN IF NOT EXISTS external_id VARCHAR(64) NULL;


    -- ==========================================
    -- Phase 1b: Preserve the Int -> UUID mission link BEFORE any Int column is dropped.
    --   Single pass over ALL child tables so there is provably exactly ONE mission per external id:
    --     1. gather every referenced external id with its best-known mission UUID;
    --     2. (A) link that external id onto the mission row it already points at;
    --     3. (B) create one synthetic mission for each external id still unmapped;
    --   only then is the unique index built and Phase 2's mapping run.
    --   Guarded on the integer mission_id columns so a re-run (already-UUID state) is a no-op.
    -- ==========================================

    -- Guarded on the integer mission_id columns: this migration swaps all child tables together
    -- in one transaction, so if mission_general_info is still Int, they all are; on a re-run
    -- (already-UUID state) the guard is false and Phase 1b is skipped entirely.
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'mission_general_info' AND column_name = 'mission_id' AND data_type = 'integer') THEN

        -- (A) Link existing mission rows. The `refs` set is every (external id, best-known UUID)
        --     pair across all child tables; DISTINCT ON keeps ONE mission per external id,
        --     preferring a real (non-null) UUID; NOT EXISTS keeps external_id unique.
        WITH refs AS (
            SELECT mission_id::text AS ext_id, mission_id_uuid AS uuid_id FROM mission_general_info WHERE mission_id IS NOT NULL
            UNION ALL
            SELECT mission_id::text, mission_id_uuid FROM mission_crew WHERE mission_id IS NOT NULL
            UNION ALL
            SELECT mission_id::text, mission_id_uuid FROM mission_passenger WHERE mission_id IS NOT NULL
            UNION ALL
            SELECT mission_id::text, owner_id FROM mission_action WHERE mission_id IS NOT NULL
        ),
        dedup AS (
            SELECT DISTINCT ON (ext_id) ext_id, uuid_id
            FROM refs ORDER BY ext_id, uuid_id NULLS LAST
        )
        UPDATE mission m
        SET external_id = d.ext_id
        FROM dedup d
        WHERE m.id = d.uuid_id
          AND m.external_id IS NULL
          AND NOT EXISTS (SELECT 1 FROM mission mx WHERE mx.external_id = d.ext_id);
        GET DIAGNOSTICS v_linked = ROW_COUNT;

        -- (B) Create exactly one synthetic mission for each external id still unmapped.
        WITH refs AS (
            SELECT mission_id::text AS ext_id FROM mission_general_info WHERE mission_id IS NOT NULL
            UNION
            SELECT mission_id::text FROM mission_crew WHERE mission_id IS NOT NULL
            UNION
            SELECT mission_id::text FROM mission_passenger WHERE mission_id IS NOT NULL
            UNION
            SELECT mission_id::text FROM mission_action WHERE mission_id IS NOT NULL
        )
        INSERT INTO mission (id, external_id, start_datetime_utc, is_deleted, mission_source)
        SELECT gen_random_uuid(), r.ext_id, NOW(), FALSE, 'MONITORENV'
        FROM refs r
        WHERE NOT EXISTS (SELECT 1 FROM mission m WHERE m.external_id = r.ext_id);
        GET DIAGNOSTICS v_created = ROW_COUNT;

        RAISE NOTICE 'mission_uuid_unification: linked % existing missions, created % synthetic missions',
            v_linked, v_created;

        -- Recover owner_id for legacy mission_action rows (inserted NULL in V1.2025.10.15),
        -- now that every external id resolves to a mission, before mission_id is dropped.
        UPDATE mission_action ma
        SET owner_id = (SELECT m.id FROM mission m WHERE m.external_id = ma.mission_id::text LIMIT 1)
        WHERE ma.owner_id IS NULL AND ma.mission_id IS NOT NULL;

        SELECT count(*) INTO v_missing
        FROM mission_action WHERE mission_id IS NOT NULL AND owner_id IS NULL;
        IF v_missing > 0 THEN
            RAISE EXCEPTION 'mission_uuid_unification: % mission_action rows still lack owner_id after backfill', v_missing;
        END IF;
    END IF;

    -- external_id is now populated for everything referenced: enforce uniqueness and
    -- realign start_datetime_utc with the non-null MissionModel field (idempotent no-op if already set).
    CREATE UNIQUE INDEX IF NOT EXISTS idx_mission_external_id ON mission(external_id);
    ALTER TABLE mission ALTER COLUMN start_datetime_utc SET NOT NULL;


    -- ==========================================
    -- Phase 2: Add safety net columns + swap to UUID
    -- Each table is handled independently with column-existence checks.
    -- The mapping below is now guaranteed to resolve (Phase 1b created a mission per external id);
    -- a completeness check raises rather than silently leaving an orphan with a NULL mission_id.
    -- ==========================================

    -- 2a. mission_action
    --     Only owner_id (UUID) is kept as the single parent FK.
    --     Drop external_mission_id and mission_id if they exist.

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_action' AND column_name = 'external_mission_id'
    ) THEN
        ALTER TABLE mission_action DROP COLUMN external_mission_id;
    END IF;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_action' AND column_name = 'mission_id'
    ) THEN
        ALTER TABLE mission_action DROP COLUMN mission_id;
    END IF;

    -- 2b. mission_general_info
    ALTER TABLE mission_general_info ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_general_info' AND column_name = 'mission_id' AND data_type = 'integer'
    ) THEN
        -- Backup old Int values (recovery safety net)
        UPDATE mission_general_info
        SET external_mission_id = mission_id::text
        WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;

        -- Populate mission_id_uuid from mapping
        UPDATE mission_general_info mgi
        SET mission_id_uuid = (
            SELECT m.id FROM mission m WHERE m.external_id = mgi.mission_id::text LIMIT 1
        )
        WHERE mgi.mission_id IS NOT NULL AND mgi.mission_id_uuid IS NULL;

        -- Completeness check: every Int-referenced row must have mapped to a mission.
        SELECT count(*) INTO v_missing
        FROM mission_general_info WHERE mission_id IS NOT NULL AND mission_id_uuid IS NULL;
        IF v_missing > 0 THEN
            RAISE EXCEPTION 'mission_uuid_unification: % mission_general_info rows failed to map to a mission', v_missing;
        END IF;

        -- Drop old Int, rename UUID
        ALTER TABLE mission_general_info DROP COLUMN mission_id;
        ALTER TABLE mission_general_info RENAME COLUMN mission_id_uuid TO mission_id;
    ELSIF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_general_info' AND column_name = 'mission_id_uuid'
    ) THEN
        -- Int already dropped in a prior run, just rename
        ALTER TABLE mission_general_info RENAME COLUMN mission_id_uuid TO mission_id;
    END IF;
    -- If neither condition matches, mission_id is already UUID — nothing to do

    -- 2c. mission_crew
    ALTER TABLE mission_crew ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_crew' AND column_name = 'mission_id' AND data_type = 'integer'
    ) THEN
        UPDATE mission_crew
        SET external_mission_id = mission_id::text
        WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;

        UPDATE mission_crew mc
        SET mission_id_uuid = (
            SELECT m.id FROM mission m WHERE m.external_id = mc.mission_id::text LIMIT 1
        )
        WHERE mc.mission_id IS NOT NULL AND mc.mission_id_uuid IS NULL;

        SELECT count(*) INTO v_missing
        FROM mission_crew WHERE mission_id IS NOT NULL AND mission_id_uuid IS NULL;
        IF v_missing > 0 THEN
            RAISE EXCEPTION 'mission_uuid_unification: % mission_crew rows failed to map to a mission', v_missing;
        END IF;

        ALTER TABLE mission_crew DROP COLUMN mission_id;
        ALTER TABLE mission_crew RENAME COLUMN mission_id_uuid TO mission_id;
    ELSIF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_crew' AND column_name = 'mission_id_uuid'
    ) THEN
        ALTER TABLE mission_crew RENAME COLUMN mission_id_uuid TO mission_id;
    END IF;

    -- 2d. mission_passenger
    ALTER TABLE mission_passenger ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;

    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_passenger' AND column_name = 'mission_id' AND data_type = 'integer'
    ) THEN
        UPDATE mission_passenger
        SET external_mission_id = mission_id::text
        WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;

        UPDATE mission_passenger mp
        SET mission_id_uuid = (
            SELECT m.id FROM mission m WHERE m.external_id = mp.mission_id::text LIMIT 1
        )
        WHERE mp.mission_id IS NOT NULL AND mp.mission_id_uuid IS NULL;

        SELECT count(*) INTO v_missing
        FROM mission_passenger WHERE mission_id IS NOT NULL AND mission_id_uuid IS NULL;
        IF v_missing > 0 THEN
            RAISE EXCEPTION 'mission_uuid_unification: % mission_passenger rows failed to map to a mission', v_missing;
        END IF;

        ALTER TABLE mission_passenger DROP COLUMN mission_id;
        ALTER TABLE mission_passenger RENAME COLUMN mission_id_uuid TO mission_id;
    ELSIF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'mission_passenger' AND column_name = 'mission_id_uuid'
    ) THEN
        ALTER TABLE mission_passenger RENAME COLUMN mission_id_uuid TO mission_id;
    END IF;

    -- 2e. inquiry (originally cross_control)
    --     Only needs mission_id as UUID, no external_mission_id safety net needed.
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'inquiry' AND column_name = 'mission_id' AND data_type = 'integer'
    ) THEN
        ALTER TABLE inquiry DROP COLUMN mission_id;
        ALTER TABLE inquiry RENAME COLUMN mission_id_uuid TO mission_id;
    ELSIF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'inquiry' AND column_name = 'mission_id_uuid'
    ) THEN
        ALTER TABLE inquiry RENAME COLUMN mission_id_uuid TO mission_id;
    END IF;

    -- ==========================================
    -- Phase 3: Recreate indexes on new UUID mission_id columns
    -- ==========================================

    CREATE INDEX IF NOT EXISTS idx_mission_general_info_mission_id ON mission_general_info(mission_id);
    CREATE INDEX IF NOT EXISTS idx_mission_crew_mission_id ON mission_crew(mission_id);
    CREATE INDEX IF NOT EXISTS idx_mission_passenger_mission_id ON mission_passenger(mission_id);
    CREATE INDEX IF NOT EXISTS idx_inquiry_mission_id ON inquiry(mission_id);

END
$$;
