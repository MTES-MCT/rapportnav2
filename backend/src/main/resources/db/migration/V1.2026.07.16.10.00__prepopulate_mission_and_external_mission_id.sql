-- Prepopulate & sync the local `mission` table with MonitorEnv external ids, and add an
-- external_mission_id safety-net column (= current Int mission_id) on child tables.
-- NON-destructive: no column dropped/retyped. The Int->UUID swap is a LATER step. Idempotent.
DO $$
DECLARE
    v_linked  bigint;
    v_created bigint;
BEGIN
    -- Phase 1: prepare mission table
    ALTER TABLE mission ALTER COLUMN service_id DROP NOT NULL;          -- env-mirror rows have no local service
    ALTER TABLE mission ADD COLUMN IF NOT EXISTS external_id VARCHAR(64) NULL;

    -- Phase 1b (A): link existing mission rows already referenced by a child's UUID.
    WITH refs AS (
        SELECT mission_id::text AS ext_id, mission_id_uuid AS uuid_id FROM mission_general_info WHERE mission_id IS NOT NULL
        UNION ALL SELECT mission_id::text, mission_id_uuid FROM mission_crew      WHERE mission_id IS NOT NULL
        UNION ALL SELECT mission_id::text, mission_id_uuid FROM mission_passenger WHERE mission_id IS NOT NULL
        UNION ALL SELECT mission_id::text, owner_id        FROM mission_action    WHERE mission_id IS NOT NULL
    ),
    dedup AS (SELECT DISTINCT ON (ext_id) ext_id, uuid_id FROM refs ORDER BY ext_id, uuid_id NULLS LAST)
    UPDATE mission m SET external_id = d.ext_id
    FROM dedup d
    WHERE m.id = d.uuid_id AND m.external_id IS NULL
      AND NOT EXISTS (SELECT 1 FROM mission mx WHERE mx.external_id = d.ext_id);
    GET DIAGNOSTICS v_linked = ROW_COUNT;

    -- Phase 1b (B): create one synthetic mission per still-unmapped external id.
    WITH refs AS (
        SELECT mission_id::text AS ext_id FROM mission_general_info WHERE mission_id IS NOT NULL
        UNION SELECT mission_id::text FROM mission_crew      WHERE mission_id IS NOT NULL
        UNION SELECT mission_id::text FROM mission_passenger WHERE mission_id IS NOT NULL
        UNION SELECT mission_id::text FROM mission_action    WHERE mission_id IS NOT NULL
    )
    INSERT INTO mission (id, external_id, start_datetime_utc, is_deleted, mission_source)
    SELECT gen_random_uuid(), r.ext_id, NOW(), FALSE, 'MONITORENV'
    FROM refs r
    WHERE NOT EXISTS (SELECT 1 FROM mission m WHERE m.external_id = r.ext_id);
    GET DIAGNOSTICS v_created = ROW_COUNT;
    RAISE NOTICE 'prepopulate mission: linked %, created % synthetic', v_linked, v_created;

    CREATE UNIQUE INDEX IF NOT EXISTS idx_mission_external_id ON mission(external_id);  -- NULLs are distinct

    -- Child safety-net column: external_mission_id = current Int mission_id (as text). general_info/crew/passenger only.
    ALTER TABLE mission_general_info ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;
    UPDATE mission_general_info SET external_mission_id = mission_id::text WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;

    ALTER TABLE mission_crew ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;
    UPDATE mission_crew SET external_mission_id = mission_id::text WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;

    ALTER TABLE mission_passenger ADD COLUMN IF NOT EXISTS external_mission_id VARCHAR(64) NULL;
    UPDATE mission_passenger SET external_mission_id = mission_id::text WHERE mission_id IS NOT NULL AND external_mission_id IS NULL;
END $$;
