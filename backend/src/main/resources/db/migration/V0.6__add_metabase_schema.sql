DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'metabase') THEN
        CREATE SCHEMA metabase;
    END IF;
END $$;
