DO $$
BEGIN
    ALTER TABLE infraction DROP COLUMN formal_notice;
    ALTER TABLE infraction ADD COLUMN formal_notice varchar(8);
END $$;
