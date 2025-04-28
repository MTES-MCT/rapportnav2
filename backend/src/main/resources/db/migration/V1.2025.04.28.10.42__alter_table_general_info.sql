DO
$$
BEGIN
    -- Make mission_id column nullable
    ALTER TABLE mission_general_info
    ALTER COLUMN mission_id DROP NOT NULL;

    -- Add the mission_id_string column
    ALTER TABLE mission_general_info
    ADD COLUMN mission_id_string VARCHAR;
END;
$$;
