DO
$$
DECLARE
  keep_id INTEGER := 15;
  dup_id  INTEGER := 10;
BEGIN
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
