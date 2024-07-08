DO
$$
BEGIN

-- delete duplicate agents
DELETE FROM agent
WHERE id BETWEEN 93 AND 105;

INSERT INTO agent_service (service_id, agent_id, agent_role_id)
VALUES
  -- pam_iris_B
  (6, 76, 11), -- Ivan,d'Alba, Commandant
  (6, 77, 12), -- Eric,Bonnamy, Second
  (6, 78, 15), -- Joseph,Le Corre, Chef de quart
  (6, 79, 15), -- Bastien,Simonnet, Chef de quart
  (6, 80, 15), -- Philippe,Peoc'h, Chef de quart
  (6, 81, 13), -- Thierry,Tavernier, Chef mécanicien
  (6, 82, 14), -- Olivier,Peron, second mécanicien
  (6, 83, 18), -- Laurent,Boutet, Agent mécanicien
  (6, 84, 18), -- Bruno,Achondo, Agent mécanicien
  (6, 85, 18), -- Fabien,Roman, Agent mécanicien
  (6, 86, 16), -- Dominique,Maingraud, maitre equipage
  (6, 87, 17), -- Thierry,Le Berrigaud, Agent pont
  (6, 88, 17), -- Claude,Guyonvarch, Agent pont
  (6, 89, 17), -- Corentin,Peron, Agent pont
  (6, 90, 17), -- Emmanuel,Ferdinand, Agent pont
  (6, 91, 21); -- Jean-Philippe,Clauzet, Ref armes




END $$;
