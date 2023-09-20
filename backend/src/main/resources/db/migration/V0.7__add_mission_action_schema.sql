DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action') THEN
        CREATE TABLE mission_action (
            id SERIAL PRIMARY KEY,
            mission_id INT NOT NULL,
            action_start_datetime_utc TIMESTAMP NOT NULL,
            action_end_datetime_utc TIMESTAMP,
            action_type VARCHAR(64) NOT NULL
        );
    END IF;
END $$;
