DO
$$
DECLARE
  keep_id INTEGER := 15;
  dup_id  INTEGER := 10;
BEGIN
  -- 0) Guard: this merge targets prod-only data (keeper id 15 is created at
  --    runtime, not seeded by migrations). In environments where either side of
  --    the merge is absent (CI, test-containers, local, fresh DBs) there is
  --    nothing to merge, so skip cleanly instead of aborting the whole run.
  IF NOT EXISTS (SELECT 1 FROM service WHERE id = keep_id AND deleted_at IS NULL) THEN
    RAISE NOTICE 'merge skipped: keeper service id % not found (or soft-deleted)', keep_id;
    RETURN;
  END IF;
  IF NOT EXISTS (SELECT 1 FROM service WHERE id = dup_id) THEN
    RAISE NOTICE 'merge skipped: duplicate service id % not found', dup_id;
    RETURN;
  END IF;

  -- 1) Simple re-points (no unique/composite-key risk)
  UPDATE "user"               SET service_id = keep_id WHERE service_id = dup_id;
  UPDATE mission              SET service_id = keep_id WHERE service_id = dup_id;
  UPDATE mission_general_info SET service_id = keep_id WHERE service_id = dup_id;
  UPDATE agent_2              SET service_id = keep_id WHERE service_id = dup_id;
  UPDATE inquiry              SET service_id = keep_id WHERE service_id = dup_id;

  -- 2) Composite-PK join tables: move rows that don't already exist on keeper, then drop the rest
  UPDATE service_control_unit scu
     SET service_id = keep_id
   WHERE scu.service_id = dup_id
     AND NOT EXISTS (SELECT 1 FROM service_control_unit k
                      WHERE k.service_id = keep_id AND k.control_unit_id = scu.control_unit_id);
  DELETE FROM service_control_unit WHERE service_id = dup_id;

  -- 3) Soft-delete the now-unreferenced duplicate service row (keep the row, flag it deleted)
  UPDATE service SET deleted_at = now() WHERE id = dup_id;
END
$$;
