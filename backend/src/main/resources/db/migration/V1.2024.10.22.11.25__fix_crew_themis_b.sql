DO
$$
  BEGIN
    UPDATE agent
    SET first_name = 'Stéphane',
        last_name  = 'Hellio'
    WHERE id = 37;

  DELETE FROM agent_service
         WHERE agent_id = 37
         AND service_id = 1
         AND agent_role_id = 11;
  END
$$;
