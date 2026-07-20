-- Backfill mission_action.owner_id (the local mission UUID) using the legacy Int mission_id
-- as the bridge:
--     mission_action.mission_id = mission.external_id  ->  owner_id = mission.id
--
-- Runs after V1.2026.07.16 has populated mission.external_id. Only fills rows whose owner_id is
-- still NULL, so it's safe to re-run and never overwrites an already-linked action.
-- mission.external_id is uniquely indexed, so each mission_id maps to at most one mission row.
-- NON-destructive, idempotent.
DO $$
DECLARE
    v_filled bigint;
BEGIN
    UPDATE mission_action ma
    SET owner_id = m.id
    FROM mission m
    WHERE ma.owner_id IS NULL
      AND ma.mission_id IS NOT NULL
      AND m.external_id = ma.mission_id::text;

    GET DIAGNOSTICS v_filled = ROW_COUNT;
    RAISE NOTICE 'backfill mission_action.owner_id: filled % rows', v_filled;
END $$;
