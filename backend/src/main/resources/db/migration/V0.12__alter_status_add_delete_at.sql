DO $$
BEGIN
    ALTER TABLE mission_action_status
    ADD COLUMN deleted_at TIMESTAMP;
END $$;
