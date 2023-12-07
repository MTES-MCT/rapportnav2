DO $$
BEGIN

    DELETE FROM agent_role;

    INSERT INTO agent_role (title) VALUES
                                   ('Commandant'),
                                   ('Second'),
                                   ('Chef mécanicien'),
                                   ('Second mécanicien'),
                                   ('Chef de quart'),
                                   ('Maître d’équipage'),
                                   ('Agent pont'),
                                   ('Agent mécanicien'),
                                   ('Cuisinier'),
                                   ('Électricien'),
                                   ('Référent armes'),
                                   ('Chef de plongée');
END $$;