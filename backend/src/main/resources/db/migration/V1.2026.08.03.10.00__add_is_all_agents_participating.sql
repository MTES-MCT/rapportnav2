DO
$$
BEGIN
ALTER TABLE mission_general_info ADD COLUMN IF NOT EXISTS is_all_agents_participating BOOLEAN NULL;
END
$$;
