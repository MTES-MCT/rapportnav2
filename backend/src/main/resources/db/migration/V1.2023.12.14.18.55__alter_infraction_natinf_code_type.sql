DO $$
BEGIN

    -- First, drop the existing foreign key constraint
    ALTER TABLE infraction_natinf
    DROP CONSTRAINT IF EXISTS fk_infractionnatinf_infraction;

    -- Now, alter the column to a string of 10 characters
    ALTER TABLE infraction_natinf
    ALTER COLUMN natinf_code TYPE VARCHAR(10);

    -- Finally, recreate the foreign key constraint
    ALTER TABLE infraction_natinf
    ADD CONSTRAINT fk_infractionnatinf_infraction
        FOREIGN KEY (infraction_id)
        REFERENCES public.infraction(id);

END $$;