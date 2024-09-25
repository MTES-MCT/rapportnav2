-- V4__insert_new_agents_and_roles.sql

DO
$$
  BEGIN
    -- Add crew members
    INSERT INTO agent (first_name, last_name)
    VALUES ('Pascal', 'Isore'),
           ('Jean Paul', 'Bigot'),
           ('Jean Luc', 'Derrien'),
           ('Philippe', 'Adam'),
           ('Freddy', 'Gosselin'),
           ('David', 'Moussay'),
           ('Claude', 'Dolou'),
           ('Ludovic', 'Pochet'),
           ('Bruno', 'Lannoy'),
           ('Vincent', 'Coupet'),
           ('Yohann', 'Le floch'),
           ('Dominique', 'Christensen'),
           ('Nicolas', 'Ducrocq'),
           ('Laurent', 'Divaret'),
           ('David', 'Simon maillat');

    -- Link to their correct service and role
    WITH agent_roles AS (VALUES ('Isore', 'Commandant'),
                                ('Bigot', 'Second'),
                                ('Derrien', 'Chef de quart'),
                                ('Adam', 'Chef de quart'),
                                ('Gosselin', 'Chef de quart'),
                                ('Moussay', 'Chef mécanicien'),
                                ('Dolou', 'Second mécanicien'),
                                ('Pochet', 'Maître d''équipage'),
                                ('Lannoy', 'Cuisinier'),
                                ('Coupet', 'Agent pont'),
                                ('Le floch', 'Agent pont'),
                                ('Christensen', 'Agent pont'),
                                ('Ducrocq', 'Agent mécanicien'),
                                ('Divaret', 'Agent mécanicien'),
                                ('Simon maillat', 'Agent mécanicien'))
    INSERT
    INTO agent_service (service_id, agent_id, agent_role_id)
    SELECT 1, a.id, ar.id
    FROM agent a
           JOIN agent_roles ar_temp ON a.last_name = ar_temp.column1
           JOIN agent_role ar ON ar.title = ar_temp.column2
    ORDER BY CASE a.last_name
               WHEN 'Isore' THEN 1
               WHEN 'Bigot' THEN 2
               WHEN 'Derrien' THEN 3
               WHEN 'Adam' THEN 4
               WHEN 'Gosselin' THEN 5
               WHEN 'Moussay' THEN 6
               WHEN 'Dolou' THEN 7
               WHEN 'Pochet' THEN 8
               WHEN 'Lannoy' THEN 9
               WHEN 'Coupet' THEN 10
               WHEN 'Le floch' THEN 11
               WHEN 'Christensen' THEN 12
               WHEN 'Ducrocq' THEN 13
               WHEN 'Divaret' THEN 14
               WHEN 'Simon maillat' THEN 15
               END;
  END
$$;
