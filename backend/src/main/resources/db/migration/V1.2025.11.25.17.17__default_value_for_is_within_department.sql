DO
$$
BEGIN

ALTER TABLE mission_action
  ALTER COLUMN is_within_department SET DEFAULT TRUE;

END
$$;

