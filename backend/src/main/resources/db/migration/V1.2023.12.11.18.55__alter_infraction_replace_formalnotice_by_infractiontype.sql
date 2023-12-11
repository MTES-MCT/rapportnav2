DO $$
BEGIN
    ALTER TABLE infraction DROP COLUMN IF EXISTS formal_notice;

    ALTER TABLE infraction ADD COLUMN infraction_type VARCHAR(16) ;

END $$;