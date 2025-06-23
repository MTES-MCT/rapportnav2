DO
$$
BEGIN
    ALTER TABLE mission_crew
    ALTER COLUMN mission_id DROP NOT NULL;

    ALTER TABLE mission_crew
    ADD COLUMN mission_id_uuid uuid NULL;
END;
$$;
