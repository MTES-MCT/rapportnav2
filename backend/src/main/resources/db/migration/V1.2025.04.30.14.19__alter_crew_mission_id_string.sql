DO
$$
BEGIN
    -- Make the mission_id column nullable
    ALTER TABLE mission_crew
    ALTER COLUMN mission_id DROP NOT NULL;

    -- Add the mission_id_string column
    ALTER TABLE mission_crew
    ADD COLUMN mission_id_string VARCHAR;

    -- Copy data from mission_id to mission_id_string as text
    EXECUTE 'UPDATE mission_crew SET mission_id_string = mission_id::text';
END;
$$;
