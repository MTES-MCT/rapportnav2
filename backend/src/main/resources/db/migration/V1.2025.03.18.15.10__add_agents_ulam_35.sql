DO $$
BEGIN
    -- Insérer des agents
    INSERT INTO agent (first_name, last_name) VALUES
        ('Juanita', 'Turpin'),
        ('Bastien', 'Lumet'),
        ('Michel', 'Bernard'),
        ('Cédric', 'Pruvost'),
        ('Olivier', 'Remond'),
        ('Julien', 'Maes'),
        ('Etienne', 'Troussard');

    -- Associer les agents au service 11
    INSERT INTO agent_service (agent_id, service_id)
    SELECT id, 12 FROM agent
    WHERE (first_name = 'Juanita' AND last_name = 'Turpin')
       OR (first_name = 'Bastien' AND last_name = 'Lumet')
       OR (first_name = 'Michel' AND last_name = 'Bernard')
       OR (first_name = 'Cédric' AND last_name = 'Pruvost')
       OR (first_name = 'Olivier' AND last_name = 'Remond')
       OR (first_name = 'Julien' AND last_name = 'Maes');
END $$;
