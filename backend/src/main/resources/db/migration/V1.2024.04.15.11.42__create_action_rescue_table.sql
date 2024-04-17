DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_rescue') THEN
        CREATE TABLE mission_action_rescue (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            end_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            geom GEOMETRY(MULTIPOLYGON),
            is_vessel_rescue BOOLEAN NOT NULL DEFAULT false,
            is_person_rescue BOOLEAN NOT NULL DEFAULT false,
            is_vessel_noticed BOOLEAN NOT NULL DEFAULT false,
            is_vessel_towed BOOLEAN NOT NULL DEFAULT false,
            observations TEXT,
            is_in_srr_or_followed_by_cross_mrcc BOOLEAN NOT NULL DEFAULT false,
            number_persons_rescued INT,
            number_of_deaths INT,
            operation_follows_defrep BOOLEAN NOT NULL DEFAULT false,
            location_description TEXT
        );
    END IF;
END $$;
