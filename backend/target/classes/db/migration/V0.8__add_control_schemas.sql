DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_gens_de_mer') THEN
        CREATE TABLE control_gens_de_mer (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            action_control_id UUID NOT NULL,
            confirmed BOOLEAN,
            staff_outnumbered BOOLEAN,
            up_to_date_medical_check BOOLEAN,
            knowledge_of_french_law_and_language BOOLEAN,
            observations TEXT,
            CONSTRAINT fk_control_gens_de_mer_action_id FOREIGN KEY (action_control_id) REFERENCES mission_action_control(id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_security') THEN
        CREATE TABLE control_security (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            action_control_id UUID NOT NULL,
            confirmed BOOLEAN,
            observations TEXT,
            CONSTRAINT fk_control_security_action_id FOREIGN KEY (action_control_id) REFERENCES mission_action_control(id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_navigation') THEN
        CREATE TABLE control_navigation (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            action_control_id UUID NOT NULL,
            confirmed BOOLEAN,
            observations TEXT,
            CONSTRAINT fk_control_navigation_action_id FOREIGN KEY (action_control_id) REFERENCES mission_action_control(id)
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'control_administrative') THEN
        CREATE TABLE control_administrative (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            action_control_id UUID NOT NULL,
            confirmed BOOLEAN,
            compliant_operating_permit BOOLEAN,
            up_to_date_navigation_permit BOOLEAN,
            compliant_security_documents BOOLEAN,
            observations TEXT,
            CONSTRAINT fk_control_administrative_action_id FOREIGN KEY (action_control_id) REFERENCES mission_action_control(id)
        );
    END IF;

END $$;
