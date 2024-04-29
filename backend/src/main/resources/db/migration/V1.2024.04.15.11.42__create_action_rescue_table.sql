DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_rescue') THEN
        CREATE TABLE mission_action_rescue (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            end_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
            latitude FLOAT,
            longitude FLOAT,
            is_vessel_rescue BOOLEAN,
            is_person_rescue BOOLEAN,
            is_vessel_noticed BOOLEAN,
            is_vessel_towed BOOLEAN,
            observations TEXT,
            is_in_srr_or_followed_by_cross_mrcc BOOLEAN,
            number_persons_rescued INT,
            number_of_deaths INT,
            operation_follows_defrep BOOLEAN,
            location_description TEXT,
            is_migration_rescue BOOLEAN,
            nb_vessels_tracked_without_intervention INT,
            nb_assisted_vessels_returning_to_shore INT
        );
    END IF;
END $$;
