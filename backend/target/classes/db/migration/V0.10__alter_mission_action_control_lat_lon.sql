DO $$
BEGIN
    ALTER TABLE mission_action_control
    ADD COLUMN latitude DOUBLE PRECISION,
    ADD COLUMN longitude DOUBLE PRECISION;
END $$;
