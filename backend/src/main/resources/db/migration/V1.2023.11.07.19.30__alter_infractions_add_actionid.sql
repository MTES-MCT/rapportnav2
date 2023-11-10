DO $$
BEGIN
    ALTER TABLE infraction
    ADD COLUMN action_id VARCHAR(36);
END $$;
