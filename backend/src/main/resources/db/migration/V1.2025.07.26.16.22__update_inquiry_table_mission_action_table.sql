DO
$$
  BEGIN
    ALTER TABLE IF EXISTS inquiry ADD COLUMN IF NOT EXISTS is_signed_by_inspector boolean NULL;
    ALTER TABLE IF EXISTS mission_action ADD COLUMN IF NOT EXISTS nbr_of_hours INTEGER NULL;
  END
$$;
