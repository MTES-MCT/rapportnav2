DO
$$
  BEGIN
    ALTER TABLE mission_action RENAME COLUMN mission_id_uuid TO owner_id;
  END
$$;
