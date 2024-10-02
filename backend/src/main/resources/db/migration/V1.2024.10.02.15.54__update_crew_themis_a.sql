DO
$$
  BEGIN
    INSERT INTO agent_service (agent_id, service_id, agent_role_id)
    SELECT a.id, 3, 14
    FROM agent a
    WHERE a.last_name LIKE '%Lepêtre%';

    UPDATE agent
    SET first_name = 'Benoît',
        last_name  = 'Maurizot'
    WHERE last_name = 'Morizot';

    UPDATE agent_service
    SET service_id    = 3,
        agent_role_id = 18
    WHERE agent_id IN (SELECT id
                       FROM agent
                       WHERE last_name LIKE '%Duval%');
  END
$$;
