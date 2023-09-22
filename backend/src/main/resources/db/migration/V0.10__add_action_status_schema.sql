DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_name = 'mission_action_status'
    ) THEN
        CREATE TABLE mission_action_status (
            id serial PRIMARY KEY,
            mission_id INTEGER NOT NULL,
            start_datetime_utc TIMESTAMP NOT NULL,
            is_start BOOLEAN NOT NULL,
            status VARCHAR(64) NOT NULL,
            reason VARCHAR(64),
            observations TEXT
        );
    END IF;
END $$;
