DO $$
BEGIN
    ALTER TABLE mission_action_control
    ADD COLUMN deleted_at TIMESTAMP;

    ALTER TABLE control_gens_de_mer
    ADD COLUMN deleted_at TIMESTAMP;

    ALTER TABLE control_security
    ADD COLUMN deleted_at TIMESTAMP;

    ALTER TABLE control_navigation
    ADD COLUMN deleted_at TIMESTAMP;

    ALTER TABLE control_administrative
    ADD COLUMN deleted_at TIMESTAMP;
END $$;
