DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_control') THEN
        CREATE TABLE mission_action_control (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP NOT NULL,
            end_datetime_utc TIMESTAMP
        );
    END IF;
END $$;
