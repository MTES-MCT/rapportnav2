DO $$
BEGIN
    ALTER TABLE mission_action_rescue
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_nautical_event
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_anti_pollution
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_vigimer
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_baaem_permanence
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_public_order
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_representation
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_illegal_immigration
    ADD COLUMN is_complete_for_stats BOOLEAN;
END $$;
