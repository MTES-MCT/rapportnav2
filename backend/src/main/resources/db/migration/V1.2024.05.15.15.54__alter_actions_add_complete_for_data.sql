DO $$
BEGIN
    ALTER TABLE mission_action_free_note
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_status
    ADD COLUMN is_complete_for_stats BOOLEAN;

    ALTER TABLE mission_action_control
    ADD COLUMN is_complete_for_stats BOOLEAN;
END $$;
