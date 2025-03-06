DO $$
BEGIN
    INSERT INTO agent (first_name, last_name) VALUES
        ('Cyril', 'Celle'),
        ('Michel', 'Deher'),
        ('Charles', 'Fereol-Talbot'),
        ('Jimmy', 'Feron'),
        ('Margaux', 'Gennesseaux'),
        ('Charly', 'Luissint'),
        ('Smith', 'Rubrice');

    INSERT INTO agent_service (agent_id, service_id)
    SELECT id, 10 FROM agent
    WHERE (first_name = 'Cyril' AND last_name = 'Celle')
       OR (first_name = 'Michel' AND last_name = 'Deher')
       OR (first_name = 'Charles' AND last_name = 'Fereol-Talbot')
       OR (first_name = 'Jimmy' AND last_name = 'Feron')
       OR (first_name = 'Margaux' AND last_name = 'Gennesseaux')
       OR (first_name = 'Charly' AND last_name = 'Luissint')
       OR (first_name = 'Smith' AND last_name = 'Rubrice');
END $$;

