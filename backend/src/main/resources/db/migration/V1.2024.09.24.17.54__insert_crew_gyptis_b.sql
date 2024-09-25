DO
$$
  BEGIN
    -- Add crew members
    INSERT INTO agent (first_name, last_name)
    VALUES ('Fabien', 'Arizzi'),
           ('Patrice', 'Morinet'),
           ('Romain', 'Pouvreau'),
           ('Yann', 'Callec'),
           ('Caroline', 'Radius'),
           ('Ronan', 'Le guillou'),
           ('Stéphane', 'Lehodey'),
           ('Pierrick', 'Nael'),
           ('Bruno', 'Cuvillon'),
           ('Christian', 'Piel'),
           ('Pierre', 'Alric'),
           ('Dominique', 'Pecquet'),
           ('Pierre', 'Olhagaray'),
           ('Maurice', 'Bouveret'),
           ('Herve', 'Simon'),
           ('Pascal', 'Thominet');

    -- Link to their correct service and role
    WITH agent_roles AS (VALUES ('Arizzi', 'Agent mécanicien'),
                                ('Morinet', 'Agent pont'),
                                ('Pouvreau', 'Chef de quart'),
                                ('Callec', 'Agent pont'),
                                ('Radius', 'Second mécanicien'),
                                ('Le guillou', 'Commandant'),
                                ('Lehodey', 'Maître d’équipage'),
                                ('Nael', 'Agent pont'),
                                ('Cuvillon', 'Agent pont'),
                                ('Piel', 'Chef mécanicien'),
                                ('Alric', 'Cuisinier'),
                                ('Pecquet', 'Second'),
                                ('Olhagaray', 'Agent mécanicien'),
                                ('Bouveret', 'Électricien'),
                                ('Simon', 'Chef de quart'),
                                ('Thominet', 'Chef de quart'))
    INSERT
    INTO agent_service (service_id, agent_id, agent_role_id)
    SELECT 8, a.id, ar.id
    FROM agent a
           JOIN agent_roles ar_temp ON a.last_name = ar_temp.column1
           JOIN agent_role ar ON ar.title = ar_temp.column2
    ORDER BY CASE a.last_name
               WHEN 'Arizzi' THEN 1
               WHEN 'Morinet' THEN 2
               WHEN 'Pouvreau' THEN 3
               WHEN 'Callec' THEN 4
               WHEN 'Radius' THEN 5
               WHEN 'Le guillou' THEN 6
               WHEN 'Lehodey' THEN 7
               WHEN 'Nael' THEN 8
               WHEN 'Cuvillon' THEN 9
               WHEN 'Piel' THEN 10
               WHEN 'Alric' THEN 11
               WHEN 'Pecquet' THEN 12
               WHEN 'Olhagaray' THEN 13
               WHEN 'Bouveret' THEN 14
               WHEN 'Simon' THEN 15
               WHEN 'Thominet' THEN 16
               END;
  END
$$;
