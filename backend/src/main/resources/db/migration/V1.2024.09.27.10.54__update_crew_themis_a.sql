DO
$$
  BEGIN
    UPDATE agent_service
    SET service_id = 4
    WHERE agent_id IN (SELECT id
                       FROM agent
                       WHERE last_name LIKE '%Barinka%');

    UPDATE agent_service
    SET service_id = 3
    WHERE agent_id IN (SELECT id
                       FROM agent
                       WHERE last_name LIKE '%Duval%');

    WITH inserted_agent AS (
      INSERT INTO agent (first_name, last_name)
        VALUES ('Monsieur', 'Morizot')
        RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id, 3, 15
    FROM inserted_agent;

  END
$$;
