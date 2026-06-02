DO
$$
BEGIN

  CREATE TABLE IF NOT EXISTS mission_action_agent (
    id SERIAL PRIMARY KEY,
    mission_action_id UUID NOT NULL,
    agent_id INTEGER NOT NULL,

    CONSTRAINT fk_mission_action_agent_action FOREIGN KEY (mission_action_id) REFERENCES mission_action(id) ON DELETE CASCADE,
    CONSTRAINT fk_mission_action_agent_agent FOREIGN KEY (agent_id) REFERENCES agent_2(id),
    CONSTRAINT uq_mission_action_agent UNIQUE (mission_action_id, agent_id)
  );

  CREATE INDEX IF NOT EXISTS idx_mission_action_agent_action_id
    ON mission_action_agent (mission_action_id);

END
$$;
