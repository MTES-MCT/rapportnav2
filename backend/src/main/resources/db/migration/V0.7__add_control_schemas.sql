DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_gens_de_mer') THEN
        CREATE TABLE control_gens_de_mer (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            confirmed BOOLEAN,
            staff_outnumbered BOOLEAN,
            up_to_date_medical_check BOOLEAN,
            knowledge_of_french_law_and_language BOOLEAN,
            CONSTRAINT unique_uuid_control_gens_de_mer_constraint UNIQUE (id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_equipment_security') THEN
        CREATE TABLE control_equipment_security (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            confirmed BOOLEAN,
            observations TEXT,
            CONSTRAINT unique_uuid_control_equipment_security_constraint UNIQUE (id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_navigation_rules') THEN
        CREATE TABLE control_navigation_rules (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            confirmed BOOLEAN,
            observations TEXT,
            CONSTRAINT unique_uuid_control_navigation_rules_constraint UNIQUE (id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_administrative_vessel') THEN
        CREATE TABLE control_administrative_vessel (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            confirmed BOOLEAN,
            compliant_operating_permit BOOLEAN,
            up_to_date_navigation_permit BOOLEAN,
            compliant_security_documents BOOLEAN,
            CONSTRAINT unique_uuid_control_administrative_vessel_constraint UNIQUE (id)
        );
    END IF;

END $$;
