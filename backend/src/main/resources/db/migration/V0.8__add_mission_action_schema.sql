DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action') THEN
        CREATE TABLE mission_action (
            id UUID PRIMARY KEY,
            mission_id INT NOT NULL,
            action_start_datetime_utc TIMESTAMP,
            action_end_datetime_utc TIMESTAMP,
            controls UUID[],

            CONSTRAINT unique_uuid_mission_action_constraint UNIQUE (id),
            CONSTRAINT fk_control_gens_de_mer FOREIGN KEY (controls) REFERENCES control_gens_de_mer(id),
            CONSTRAINT fk_control_equipment_security FOREIGN KEY (controls) REFERENCES control_equipment_security(id),
            CONSTRAINT fk_control_navigation_rules FOREIGN KEY (controls) REFERENCES control_navigation_rules(id),
            CONSTRAINT fk_control_administrative_vessel FOREIGN KEY (controls) REFERENCES control_administrative_vessel(id)
        );
    END IF;
END $$;
