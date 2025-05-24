DO
$$
  BEGIN
    ALTER TYPE "ActionType" ADD VALUE IF NOT EXISTS 'CROSS_CONTROL';
    ALTER TABLE control_2 DROP COLUMN IF EXISTS nbr_of_hours;
    ALTER TABLE mission_action
      ADD COLUMN IF NOT EXISTS cross_control_id         uuid  NULL,
      ADD COLUMN IF NOT EXISTS cross_control_status     character varying(32) NULL,
      ADD COLUMN IF NOT EXISTS is_signed_by_inspector     boolean               NULL,
      ADD COLUMN IF NOT EXISTS cross_control_conclusion character varying(32) NULL,
      ADD COLUMN IF NOT EXISTS cross_control_nbr_hours  integer NULL;
  END
$$;
