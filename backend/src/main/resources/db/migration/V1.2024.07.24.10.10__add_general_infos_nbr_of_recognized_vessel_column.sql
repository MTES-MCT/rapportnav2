DO $$
BEGIN
    ALTER TABLE "mission_general_info"
    ADD COLUMN nbr_of_recognized_vessel INTEGER;
END $$;
