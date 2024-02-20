DO $$
BEGIN
    -- Delete the crew table
    DROP TABLE IF EXISTS crew;

    -- Delete the crew_role table
    DROP TABLE IF EXISTS crew_role;
END $$;
