DO $$
BEGIN
    ALTER TABLE mission_action_control
--    ADD COLUMN geom geometry(MultiPolygon),
    ADD COLUMN observations text,
    ADD COLUMN vessel_identifier VARCHAR(64),
    ADD COLUMN vessel_type VARCHAR(32),
    ADD COLUMN vessel_size VARCHAR(32),
    ADD COLUMN identity_controlled_person VARCHAR(128);
END $$;
