DO $$
BEGIN
    ALTER TABLE "mission_action_anti_pollution"
    ADD COLUMN simple_brewing_operation BOOLEAN,
    ADD COLUMN anti_pol_device_deployed BOOLEAN;
END $$;
