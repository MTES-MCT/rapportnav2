DO
$$
BEGIN
    ALTER TABLE mission_general_info
    ALTER COLUMN mission_id DROP NOT NULL;

    ALTER TABLE mission_general_info
    ADD COLUMN mission_id_uuid uuid NULL;
END;
$$;
