DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_name = 'mission_action_status'
    ) THEN
        CREATE TABLE mission_action_status (
            id SERIAL PRIMARY KEY,
            mission_action_id INTEGER NOT NULL,
            start_datetime_utc TIMESTAMP NOT NULL,
            is_start BOOLEAN NOT NULL,
            status VARCHAR(64) NOT NULL,
            reason VARCHAR(64),
            observations TEXT,
            CONSTRAINT fk_mission_action_status_id FOREIGN KEY (mission_action_id) REFERENCES mission_action(id)
        );
    END IF;
END $$;
