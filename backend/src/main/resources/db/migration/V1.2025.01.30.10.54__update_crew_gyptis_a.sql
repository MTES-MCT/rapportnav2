DO
$$
  BEGIN

    WITH inserted_agent AS (
      INSERT INTO agent (first_name, last_name)
        VALUES ('Régis', 'Apack')
        RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id, 7, 16
    FROM inserted_agent;

  END
$$;
