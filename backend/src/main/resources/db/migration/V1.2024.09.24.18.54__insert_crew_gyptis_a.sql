-- V2__update_agents_and_roles.sql

DO
$$
  DECLARE
    electricien_role_id   INT;
    second_role_id        INT;
    chef_de_quart_role_id INT;
  BEGIN
    -- Get role IDs
    SELECT id INTO electricien_role_id FROM agent_role WHERE title = 'Électricien';
    SELECT id INTO second_role_id FROM agent_role WHERE title = 'Second';
    SELECT id INTO chef_de_quart_role_id FROM agent_role WHERE title = 'Chef de quart';

    -- Update Ludovic Bouteillon to Franck Guy
    UPDATE agent
    SET first_name = 'Franck',
        last_name  = 'Guy'
    WHERE first_name = 'Ludovic'
      AND last_name = 'Bouteillon';

    -- Update Serge Croville to Hervé Langlois
    UPDATE agent
    SET first_name = 'Hervé',
        last_name  = 'Langlois'
    WHERE first_name = 'Serge'
      AND last_name = 'Croville';

    -- Update Hervé Langlois's role to Électricien
    UPDATE agent_service
    SET agent_role_id = electricien_role_id
    WHERE agent_id = (SELECT id FROM agent WHERE first_name = 'Hervé' AND last_name = 'Langlois');

    -- Update Thomas Le Gall's role to Second
    UPDATE agent_service
    SET agent_role_id = second_role_id
    WHERE agent_id = (SELECT id FROM agent WHERE first_name = 'Thomas' AND last_name = 'Le Gall');

    -- Update Michel Ceres's role to Chef de quart
    UPDATE agent_service
    SET agent_role_id = chef_de_quart_role_id
    WHERE agent_id = (SELECT id FROM agent WHERE first_name = 'Michel' AND last_name = 'Ceres');

  END
$$;
