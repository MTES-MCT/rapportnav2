DO
$$
  BEGIN
    -- disable actual agent
    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-23 00:00:00'
    WHERE agent_id = 85;

    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-23 00:00:00'
    WHERE agent_id = 78;

    UPDATE agent_service
    SET disabled_at = TIMESTAMP '2025-10-23 00:00:00'
    WHERE agent_id = 87;

    UPDATE agent_service
    SET agent_role_id = 15
    WHERE agent_id = 83;

    UPDATE agent_service
    SET agent_role_id = 18
    WHERE agent_id = 88;

    UPDATE agent_service
    SET agent_role_id = 19
    WHERE agent_id = 91;

    -- insert new agent
    WITH inserted_agent AS (
    INSERT INTO agent (first_name, last_name)
    VALUES ('Romain', 'Fiant')
      RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id,6,17
    FROM inserted_agent;


  END
$$;






