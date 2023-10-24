DO $$
BEGIN
    -- Modify control_gens_de_mer table
    ALTER TABLE control_gens_de_mer
    ADD COLUMN staff_outnumbered_text VARCHAR(16);
    
    ALTER TABLE control_gens_de_mer
    ADD COLUMN up_to_date_medical_check_text VARCHAR(16);
    
    ALTER TABLE control_gens_de_mer
    ADD COLUMN knowledge_of_french_law_and_language_text VARCHAR(16);
END $$;

DO $$
BEGIN
    -- Drop columns in control_gens_de_mer
    ALTER TABLE control_gens_de_mer
    DROP COLUMN staff_outnumbered;
    
    ALTER TABLE control_gens_de_mer
    DROP COLUMN up_to_date_medical_check;
    
    ALTER TABLE control_gens_de_mer
    DROP COLUMN knowledge_of_french_law_and_language;
END $$;

DO $$
BEGIN
    -- Rename columns in control_gens_de_mer
    ALTER TABLE control_gens_de_mer
    RENAME COLUMN staff_outnumbered_text TO staff_outnumbered;
    
    ALTER TABLE control_gens_de_mer
    RENAME COLUMN up_to_date_medical_check_text TO up_to_date_medical_check;
    
    ALTER TABLE control_gens_de_mer
    RENAME COLUMN knowledge_of_french_law_and_language_text TO knowledge_of_french_law_and_language;
END $$;

DO $$
BEGIN
    -- Modify control_administrative table
    ALTER TABLE control_administrative
    ADD COLUMN compliant_operating_permit_text VARCHAR(16);
    
    ALTER TABLE control_administrative
    ADD COLUMN up_to_date_navigation_permit_text VARCHAR(16);
    
    ALTER TABLE control_administrative
    ADD COLUMN compliant_security_documents_text VARCHAR(16);
END $$;

DO $$
BEGIN
    -- Drop columns in control_administrative
    ALTER TABLE control_administrative
    DROP COLUMN compliant_operating_permit;
    
    ALTER TABLE control_administrative
    DROP COLUMN up_to_date_navigation_permit;
    
    ALTER TABLE control_administrative
    DROP COLUMN compliant_security_documents;
END $$;

DO $$
BEGIN
    -- Rename columns in control_administrative
    ALTER TABLE control_administrative
    RENAME COLUMN compliant_operating_permit_text TO compliant_operating_permit;
    
    ALTER TABLE control_administrative
    RENAME COLUMN up_to_date_navigation_permit_text TO up_to_date_navigation_permit;
    
    ALTER TABLE control_administrative
    RENAME COLUMN compliant_security_documents_text TO compliant_security_documents;
END $$;
