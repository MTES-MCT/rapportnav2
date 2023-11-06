DO $$
BEGIN
    ALTER TABLE infraction
    DROP COLUMN deleted_at;

    ALTER TABLE control_administrative
    DROP COLUMN deleted_at;
    ALTER TABLE control_security
    DROP COLUMN deleted_at;
    ALTER TABLE control_navigation
    DROP COLUMN deleted_at;
    ALTER TABLE control_gens_de_mer
    DROP COLUMN deleted_at;

    ALTER TABLE mission_action_control
    DROP COLUMN deleted_at;

    ALTER TABLE mission_action_status
    DROP COLUMN deleted_at;
END $$;
