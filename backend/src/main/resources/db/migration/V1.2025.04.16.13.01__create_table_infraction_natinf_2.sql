DO
$$
BEGIN
CREATE TABLE infraction_natinf_2
(
  id            SERIAL PRIMARY KEY,
  infraction_id uuid                  NOT NULL,
  natinf_code   character varying(10) NOT NULL
);

END
$$;
