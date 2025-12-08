DO
$$
BEGIN
CREATE TABLE establishment
(
  id                       serial PRIMARY KEY,
  siret   VARCHAR(36) NULL,
  siren   VARCHAR(36) NULL,
  name    VARCHAR(255) NULL,
  city    VARCHAR(16) NULL,
  country VARCHAR(16) NULL,
  zipcode VARCHAR(16) NULL,
  address VARCHAR(255) NULL,
  is_foreign boolean NULL,
  created_at               TIMESTAMP DEFAULT NOW(),
  updated_at               TIMESTAMP
);


ALTER TABLE inquiry DROP COLUMN IF EXISTS siren;
ALTER TABLE inquiry DROP COLUMN IF EXISTS is_foreign_establishment;
ALTER TABLE inquiry ADD COLUMN IF NOT EXISTS establishment_id integer NULL;

ALTER TABLE mission_action DROP COLUMN IF EXISTS siren;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS establishment_id integer NULL;

END
$$;
