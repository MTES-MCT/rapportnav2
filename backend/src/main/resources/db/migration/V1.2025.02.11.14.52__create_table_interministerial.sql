CREATE TABLE inter_ministerial_service (
    id SERIAL PRIMARY KEY,
    administration_id INT NOT NULL,
    control_unit_id INT NOT NULL,
    mission_general_info_id INT,
    CONSTRAINT fk_mission_general_info
        FOREIGN KEY (mission_general_info_id)
        REFERENCES mission_general_info(id)
        ON DELETE SET NULL
);
