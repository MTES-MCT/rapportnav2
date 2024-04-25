DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_illegal_immigration') THEN
        CREATE TABLE mission_action_illegal_immigration (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            end_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            observations TEXT,
            nb_of_intercepted_vessels INT,
            nb_of_intercepted_migrants INT,
            nb_of_suspected_smugglers INT,
            latitude FLOAT,
            longitude FLOAT
        );
    END IF;
END $$;
