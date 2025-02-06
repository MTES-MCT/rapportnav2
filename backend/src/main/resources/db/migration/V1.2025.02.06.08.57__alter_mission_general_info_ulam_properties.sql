DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'is_with_interministerial_service') THEN
        ALTER TABLE mission_general_info ADD COLUMN is_with_interministerial_service BOOLEAN NULL DEFAULT FALSE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'is_mission_armed') THEN
        ALTER TABLE mission_general_info ADD COLUMN is_mission_armed BOOLEAN NULL DEFAULT FALSE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'mission_report_type') THEN
        ALTER TABLE mission_general_info ADD COLUMN mission_report_type VARCHAR NULL;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'reinforcement_type') THEN
        ALTER TABLE mission_general_info ADD COLUMN reinforcement_type VARCHAR NULL;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'nb_hour_at_sea') THEN
        ALTER TABLE mission_general_info ADD COLUMN nb_hour_at_sea INT NULL;
    END IF;
END $$;
