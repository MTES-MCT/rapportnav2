DO
$$
BEGIN
CREATE TABLE establishment
(
  id                       serial PRIMARY KEY,
  siret                    integer NULL,
  siren                    integer NULL,
  name                     character varying(255) NOT NULL,
  city                     character varying(16) NULL,
  country                  character varying(16) NULL,
  zipcode                  character varying(16) NULL,
  address                  character varying(255) NULL,
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
