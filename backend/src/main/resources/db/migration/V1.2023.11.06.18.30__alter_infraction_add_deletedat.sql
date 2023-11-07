DO $$
BEGIN
    ALTER TABLE infraction
    ADD COLUMN deleted_at TIMESTAMP;
END $$;
