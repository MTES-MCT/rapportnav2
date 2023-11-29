
DROP TABLE agent_crew;

CREATE TABLE mission_crew (
    id SERIAL PRIMARY KEY,
    agent_id INT,
    comment VARCHAR(255) DEFAULT NULL,
    agent_role_id INT NOT NULL,
    mission_id INT DEFAULT NULL,
    FOREIGN KEY (agent_id) REFERENCES agent(id),
    FOREIGN KEY (agent_role_id) REFERENCES agent_role(id)
);
