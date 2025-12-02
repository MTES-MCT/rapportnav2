DO
$$
BEGIN
ALTER TABLE inquiry
  ADD COLUMN IF NOT EXISTS is_foreign_establishment boolean NULL;
END
$$;
