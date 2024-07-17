DO $$
BEGIN
  ALTER TABLE mission_action_anti_pollution
    ADD COLUMN latitude DOUBLE PRECISION,
    ADD COLUMN longitude DOUBLE PRECISION,
    ADD COLUMN detected_pollution BOOLEAN,
    ADD COLUMN pollution_observed_by_authorized_agent BOOLEAN,
    ADD COLUMN diversion_carried_out BOOLEAN;
END $$;
