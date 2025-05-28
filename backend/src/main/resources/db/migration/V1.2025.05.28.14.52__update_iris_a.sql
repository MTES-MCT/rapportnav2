DO
$$
  BEGIN
    -- disable actual agent
    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2024-07-31 00:00:00'
    WHERE agent_id = 24;

    -- insert new agent
    WITH inserted_agent AS (
    INSERT INTO agent (first_name, last_name)
    VALUES ('Erwan', 'Legall')
      RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,5,17
    FROM inserted_agent;

  END
$$;






