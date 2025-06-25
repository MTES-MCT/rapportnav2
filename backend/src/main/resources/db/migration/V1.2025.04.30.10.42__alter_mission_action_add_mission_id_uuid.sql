DO
$$
BEGIN
    -- Check if the table mission_action exists
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action') THEN

        ALTER TABLE mission_action
        ADD COLUMN IF NOT EXISTS mission_id_uuid uuid NULL;
    END IF;
END
$$;
