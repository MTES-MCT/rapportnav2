DO
$$
  BEGIN
    ALTER TYPE "ActionType" ADD VALUE IF NOT EXISTS 'CROSSED_CONTROL';
    ALTER TABLE control_2 DROP COLUMN IF EXISTS nbr_of_hours;
    ALTER TABLE mission_action
      ADD COLUMN IF NOT EXISTS crossed_control_id         character varying(16) NULL,
      ADD COLUMN IF NOT EXISTS crossed_control_status     character varying(16) NULL,
      ADD COLUMN IF NOT EXISTS is_signed_by_inspector     boolean               NULL,
      ADD COLUMN IF NOT EXISTS crossed_control_conclusion character varying(16) NULL,
      ADD COLUMN IF NOT EXISTS crossed_control_nbr_hours  integer NULL;
  END
$$;
