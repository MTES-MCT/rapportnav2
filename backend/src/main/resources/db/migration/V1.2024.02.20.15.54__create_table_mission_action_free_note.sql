DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_free_note') THEN
        CREATE TABLE mission_action_free_note (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            end_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            observations TEXT NOT NULL
        );
    END IF;
END $$;
