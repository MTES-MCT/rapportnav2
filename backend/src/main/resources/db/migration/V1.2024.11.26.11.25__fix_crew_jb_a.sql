DO
$$
  BEGIN
    INSERT INTO agent_service (service_id, agent_id, agent_role_id)
    VALUES (1, 129, 16);

    DELETE
    FROM agent_service
    WHERE agent_id = 40
      AND service_id = 1;

    DELETE
    FROM agent_service
    WHERE agent_id = 122
      AND service_id = 1;

  END
$$;
