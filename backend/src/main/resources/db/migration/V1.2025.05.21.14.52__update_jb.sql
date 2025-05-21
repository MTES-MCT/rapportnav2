DO
$$
  BEGIN
    -- disable actual agent
    UPDATE agent_service
    SET disabled_at = NOW()
    WHERE agent_id = 48;

    -- insert new agent
    WITH inserted_agent AS (
    INSERT INTO agent (first_name, last_name)
    VALUES ('Stéphane', 'Devey')
      RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,1,11
    FROM inserted_agent;

  END
$$;






