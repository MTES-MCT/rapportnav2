DO
$$
BEGIN
    -- Make the mission_id column nullable
    ALTER TABLE mission_general_info
    ALTER COLUMN mission_id DROP NOT NULL;

    -- Add the mission_id_string column
    ALTER TABLE mission_general_info
    ADD COLUMN mission_id_string VARCHAR;

    -- Copy data from mission_id to mission_id_string as text
    EXECUTE 'UPDATE mission_general_info SET mission_id_string = mission_id::text';
END;
$$;
