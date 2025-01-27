DO
$$
  BEGIN

    WITH inserted_agent AS (
      INSERT INTO agent (first_name, last_name)
        VALUES ('José', 'Lenoir')
        RETURNING id)
    INSERT
    INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT id, 1, 17
    FROM inserted_agent;

  END
$$;
