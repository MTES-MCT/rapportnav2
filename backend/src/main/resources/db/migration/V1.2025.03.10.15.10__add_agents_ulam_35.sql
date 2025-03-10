DO $$
BEGIN
    -- Insérer des agents
    INSERT INTO agent (first_name, last_name) VALUES
        ('Etienne', 'Troussard'),
        ('Sylvain', 'Pincon'),
        ('Vanessa', 'Guerin'),
        ('Aude', 'Delarose'),
        ('Emeric', 'Maleuvre'),
        ('Bernard', 'Carneiro'),
        ('Stéphane', 'Courdent');

    -- Associer les agents au service 11
    INSERT INTO agent_service (agent_id, service_id)
    SELECT id, 11 FROM agent
    WHERE (first_name = 'Etienne' AND last_name = 'Troussard')
       OR (first_name = 'Sylvain' AND last_name = 'Pincon')
       OR (first_name = 'Vanessa' AND last_name = 'Guerin')
       OR (first_name = 'Aude' AND last_name = 'Delarose')
       OR (first_name = 'Emeric' AND last_name = 'Maleuvre')
       OR (first_name = 'Bernard' AND last_name = 'Carneiro')
       OR (first_name = 'Stéphane' AND last_name = 'Courdent');
END $$;
