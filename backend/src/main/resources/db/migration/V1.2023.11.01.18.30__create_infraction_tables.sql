DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'infraction') THEN
        CREATE TABLE infraction (
            id UUID PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            control_id UUID NOT NULL,
            control_type VARCHAR NOT NULL,
            formal_notice BOOLEAN,
            observations VARCHAR
        );
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'infraction_natinf') THEN
       CREATE TABLE infraction_natinf (
           infraction_id UUID NOT NULL,
           natinf_code INT NOT NULL,

           PRIMARY KEY (infraction_id, natinf_code),

           -- Define the foreign key constraint for the many-to-one relationship with infraction
           CONSTRAINT fk_infractionnatinf_infraction FOREIGN KEY (infraction_id) REFERENCES infraction (id)
       );
    END IF;
END $$;
