DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_action_control') THEN
        CREATE TABLE mission_action_control (
            id SERIAL PRIMARY KEY,
            mission_action_id INT NOT NULL,
            CONSTRAINT fk_mission_action_control_id FOREIGN KEY (mission_action_id) REFERENCES mission_action(id)

        );
    END IF;
END $$;
