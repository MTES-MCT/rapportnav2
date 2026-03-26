DO
$$
  BEGIN
    ALTER TABLE mission_action ALTER COLUMN training_type TYPE VARCHAR(255);
  END
$$;
