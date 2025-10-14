DO
$$
  BEGIN
    -- disable actual agent
    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-14 00:00:00'
    WHERE agent_id = 22;

    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-14 00:00:00'
    WHERE agent_id = 116;

    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-14 00:00:00'
    WHERE agent_id = 106;

    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-14 00:00:00'
    WHERE agent_id = 121;

    -- insert new agent
    WITH inserted_agent AS (
    INSERT INTO agent (first_name, last_name)
    VALUES ('Bruno', 'Fouchard')
      RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,8,19
    FROM inserted_agent;

    -- insert new agent
    WITH inserted_agent AS (
      INSERT INTO agent (first_name, last_name)
        VALUES ('Fabien', 'Roman')
        RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,8,18
    FROM inserted_agent;

    -- insert new agent
    WITH inserted_agent AS (
      INSERT INTO agent (first_name, last_name)
        VALUES ('Samuel', 'Alsfasser')
        RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,8,15
    FROM inserted_agent;

  END
$$;






