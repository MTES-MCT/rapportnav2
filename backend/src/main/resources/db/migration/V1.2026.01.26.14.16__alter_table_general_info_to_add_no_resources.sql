DO
$$
BEGIN
ALTER TABLE mission_general_info ADD COLUMN IF NOT EXISTS is_resources_not_used BOOLEAN NULL;
END
$$;
