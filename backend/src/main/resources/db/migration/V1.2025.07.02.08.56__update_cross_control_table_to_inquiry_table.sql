DO
$$
  BEGIN
    ALTER TABLE cross_control ADD COLUMN IF NOT EXISTS mission_id INTEGER NULL;
    ALTER TABLE cross_control ADD COLUMN IF NOT EXISTS mission_id_uuid uuid NULL;
    ALTER TABLE IF EXISTS cross_control RENAME TO inquiry;
    ALTER TABLE mission_action DROP COLUMN IF EXISTS cross_control_id ;
    ALTER TABLE mission_action DROP COLUMN IF EXISTS cross_control_status;
    ALTER TABLE mission_action DROP COLUMN IF EXISTS is_signed_by_inspector;
    ALTER TABLE mission_action DROP COLUMN IF EXISTS cross_control_conclusion;
    ALTER TABLE mission_action DROP COLUMN IF EXISTS cross_control_nbr_hours;
  END
$$;
