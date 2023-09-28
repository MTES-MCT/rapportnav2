DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_control') THEN
        CREATE TABLE mission_action_control (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP NOT NULL,
            end_datetime_utc TIMESTAMP,
            control_method VARCHAR(16) NOT NULL,
            vessel_identifier VARCHAR(64) NOT NULL,
            vessel_type VARCHAR(32) NOT NULL,
            vessel_size VARCHAR(32) NOT NULL,
            identity_controlled_person VARCHAR(128) NOT NULL,
            observations TEXT
        );
    END IF;
END $$;
