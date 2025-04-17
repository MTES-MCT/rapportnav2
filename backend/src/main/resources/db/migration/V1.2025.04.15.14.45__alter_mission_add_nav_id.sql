DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission') THEN
        ALTER TABLE mission
        ADD COLUMN IF NOT EXISTS nav_id UUID;
    END IF;
END$$;
