DO $$
BEGIN
    ALTER TABLE "user"
    ADD COLUMN first_name VARCHAR(64),
    ADD COLUMN last_name VARCHAR(64);

    UPDATE "user"
    SET
      first_name = name,
      last_name = name;

    ALTER TABLE "user"
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN last_name SET NOT NULL;

    ALTER TABLE "user"
    DROP COLUMN name;
END $$;
