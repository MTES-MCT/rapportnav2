DO $$
BEGIN
    -- For the vessel_identifier column
    ALTER TABLE mission_action_control
    ALTER COLUMN vessel_identifier DROP NOT NULL;

    -- For the vessel_type column
    ALTER TABLE mission_action_control
    ALTER COLUMN vessel_type DROP NOT NULL;

    -- For the vessel_size column
    ALTER TABLE mission_action_control
    ALTER COLUMN vessel_size DROP NOT NULL;

    -- For the identity_controlled_person column
    ALTER TABLE mission_action_control
    ALTER COLUMN identity_controlled_person DROP NOT NULL;

END $$;
