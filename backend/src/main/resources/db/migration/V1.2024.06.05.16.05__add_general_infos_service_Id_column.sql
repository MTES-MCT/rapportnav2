DO $$
BEGIN
    ALTER TABLE "mission_general_info"
    ADD COLUMN service_id INTEGER;
END $$;
