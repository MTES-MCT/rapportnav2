DO
$$
BEGIN
DELETE FROM agent_service;
    -- Insert into 'agent_service' table
INSERT INTO agent_service (service_id, agent_id, agent_role_id)
VALUES
  -- pam_jeanne_barret_B
  (2, 1, 11),  -- Christian Sauvage, Commandant
  (2, 2, 12),  -- François Dambron, Second
  (2, 3, 13),  -- Frédéric Grégoire, Chef mécanicien
  (2, 4, 14),  -- Nicolas Delattre, Second mécanicien
  (2, 5, 15),  -- Jean François Albert, Chef de quart
  (2, 6, 15),  -- Guillaume Gatoux, Chef de quart
  (2, 7, 16),  -- Arnaud Celton, Maître d’équipage
  (2, 8, 17),  -- Francis Beyaert, Agent pont
  (2, 9, 17),  -- Gaylord Suret, Agent pont
  (2, 10, 17), -- Thierry Lesaulnier, Agent pont
  (2, 11, 17), -- Laurent Duval, Agent pont
  (2, 12, 19), -- Philippe Bon, Cuisinier
  (2, 13, 18), -- Adeline Lagadec, Agent mécanicien
  (2, 14, 18), -- Pascal Morel, Agent mécanicien
  (2, 15, 18), -- Yann Mollat, Agent mécanicien

  -- pam_iris_A
  (5, 16, 11), -- Xavier Lacourrege, Commandant
  (5, 17, 12), -- Thierry Crochard, Second
  (5, 18, 13), -- Marc Ottini, Chef mécanicien
  (5, 19, 14), -- Gilles Menuge, Second mécanicien
  (5, 20, 15), -- Jean-David Duhaudt, Chef de quart
  (5, 21, 15), -- Stephane Bistour, Chef de quart
  (5, 22, 15), -- Herve Simon, Chef de quart
  (5, 23, 17), -- Fabien Roman, Agent pont (unknown)
  (5, 24, 17), -- Stéphane Lelièvre, Agent pont (unknown)
  (5, 25, 18), -- Yves Maniette, Agent mécanicien
  (5, 26, 18), -- Gaétan Merrien, Agent mécanicien
  (5, 27, 16), -- Frédéric Beyaert, Maître d’équipage
  (5, 28, 17), -- Franck Touron, Agent pont
  (5, 29, 17), -- Frédéric Dechaine, Agent pont
  (5, 30, 19), -- Francois Le Bon, Cuisinier

  -- pam_themis_A
  (3, 31, 11), -- Ariane Regaud, Commandant
  (3, 32, 12), -- Philippe Gahinet, Second
  (3, 33, 13), -- Didier Cozic, Chef mécanicien
  (3, 34, 14), -- Régis Soubise, Second mécanicien
  (3, 35, 15), -- Philippe Davies, Chef de quart
  (3, 36, 15), -- Franck Grouiec, Chef de quart
  (3, 37, 15), -- Stéphane Hellio, Chef de quart
  (3, 38, 16), -- Pascal Rousselet, Maître d’équipage
  (3, 39, 17), -- Laurent Forgeard, Agent pont
  (3, 40, 17), -- Oswald Isore, Agent pont
  (3, 41, 17), -- Yvan Pavie, Agent pont
  (3, 42, 17), -- Thierry Yhuel, Agent pont
  (3, 43, 20), -- Stéphane Horel, Electricien
  (3, 44, 18), -- Pascal Barinka, Agent mécanicien
  (3, 45, 18), -- Nicolas Daden, Agent mécanicien
  (3, 46, 19), -- Cyril Le Scoarnec, Cuisinier

  -- pam_themis_B
  (4, 47, 11), -- Frédéric Schneider, Commandant
  (4, 48, 12), -- Pascal Isore, Second
  (4, 49, 13), -- Philippe Fournier, Chef mécanicien
  (4, 50, 14), -- Régis Tertu, Second mécanicien
  (4, 51, 15), -- Raymond Cacitti, Chef de quart
  (4, 52, 15), -- Philippe Darsu, Chef de quart
  (4, 53, 15), -- Michel Marchand, Chef de quart
  (4, 54, 16), -- Cyril Lelandois, Maître d’équipage
  (4, 55, 17), -- Jean Marc Bourbigot, Agent pont
  (4, 56, 17), -- David Collomp, Agent pont
  (4, 57, 17), -- Jérome Fievet, Agent pont
  (4, 58, 17), -- Brian O’Rorke, Agent pont
  (4, 59, 20), -- Ludovic Daoulas, Électricien
  (4, 60, 18), -- Loup Genty, Agent mécanicien
  (4, 61, 19), -- JL Chapovaloff, Cuisinier

  -- pam_gyptis_A
  (7, 62, 11), -- Ludovic Bouteillon, Commandant
  (7, 63, 12), -- Serge Croville, Second
  (7, 64, 15), -- Sylvain Rebeyrotte, Chef de quart
  (7, 65, 15), -- Thomas Le Gall, Chef de quart
  (7, 66, 13), -- Lilian Roue, Chef mécanicien
  (7, 67, 14), -- Laurent Paronneau, Second mécanicien
  (7, 68, 18), -- Nicolas Peyre, Agent mécanicien
  (7, 69, 18), -- José Maillot, Agent mécanicien
  (7, 70, 16), -- Michel Ceres, Maître d’équipage
  (7, 71, 17), -- David Demilly, Agent pont
  (7, 72, 17), -- Anne Zamaron, Agent pont
  (7, 73, 17), -- Cyrille Carval, Agent pont
  (7, 74, 17), -- Guillaume Mardelle, Agent pont
  (7, 75, 19); -- Pierre Buriez, Cuisinier

END $$;
