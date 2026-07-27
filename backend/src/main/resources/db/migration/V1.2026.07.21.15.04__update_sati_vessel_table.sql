DO
$$
BEGIN
ALTER TABLE "sati_vessel" ALTER COLUMN is_master_owner DROP NOT NULL;
ALTER TABLE "sati_vessel" ALTER COLUMN is_master_owner DROP DEFAULT;
END
$$;
