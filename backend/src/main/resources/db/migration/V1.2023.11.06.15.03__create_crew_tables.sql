CREATE TABLE agent (
    id SERIAL PRIMARY KEY,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        deleted_at TIMESTAMP
);

CREATE TABLE agent_role (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE crew (
    id SERIAL PRIMARY KEY,
        comment VARCHAR(255),
        agent_role_id INT NOT NULL,
        mission_id INT,
        FOREIGN KEY (agent_role_id) REFERENCES agent_role(id)
);


CREATE TABLE service (
   id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE agent_service (
    agent_id INT,
    service_id INT,
    FOREIGN KEY (agent_id) REFERENCES agent(id),
    FOREIGN KEY (service_id) REFERENCES service(id),
    PRIMARY KEY (agent_id, service_id)
);

CREATE TABLE agent_crew (
    agent_id INT,
    crew_id INT,
    FOREIGN KEY (agent_id) REFERENCES agent(id),
    FOREIGN KEY (crew_id) REFERENCES crew(id),
    PRIMARY KEY (agent_id, crew_id)
);
