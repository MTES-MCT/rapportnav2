DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'mission_general_info' AND column_name = 'jdp_type') THEN
        ALTER TABLE mission_general_info ADD COLUMN jdp_type VARCHAR(255);
    END IF;
END $$;
