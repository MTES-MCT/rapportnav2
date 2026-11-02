DO
$$
BEGIN
ALTER TABLE sati_establishment DROP COLUMN IF EXISTS is_not_listed;
ALTER TABLE sati_establishment DROP COLUMN IF EXISTS is_not_same_address;
ALTER TABLE sati_establishment DROP COLUMN IF EXISTS address;
ALTER TABLE sati_establishment ADD COLUMN IF NOT EXISTS establishment_id INTEGER NULL;

IF EXISTS (SELECT 1
           FROM information_schema.columns
           WHERE table_schema = current_schema()
             AND table_name = 'establishment'
             AND column_name = 'is_foreign') THEN
  ALTER TABLE establishment RENAME COLUMN is_foreign TO is_not_listed;
END IF;
ALTER TABLE establishment ADD COLUMN IF NOT EXISTS is_not_same_address BOOLEAN NOT NULL DEFAULT false;
END
$$;
