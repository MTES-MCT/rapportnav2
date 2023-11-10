DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_name = 'infraction_env_target'
    ) THEN
        CREATE TABLE infraction_env_target (
            id UUID PRIMARY KEY,
            mission_id INTEGER NOT NULL,
            action_id VARCHAR(36) NOT NULL,
            infraction_id UUID NOT NULL,
            vessel_identifier VARCHAR(64) NOT NULL,
            identity_controlled_person VARCHAR(128) NOT NULL,
            vessel_type VARCHAR(32) NOT NULL,
            vessel_size VARCHAR(32) NOT NULL
        );
    END IF;
END $$;
