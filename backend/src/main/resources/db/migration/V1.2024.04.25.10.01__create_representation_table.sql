DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_representation') THEN
        CREATE TABLE mission_action_representation (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            end_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            observations TEXT
        );
    END IF;
END $$;
