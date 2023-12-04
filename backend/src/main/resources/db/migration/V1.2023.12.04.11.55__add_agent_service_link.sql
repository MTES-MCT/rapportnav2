DO $$
BEGIN
    -- Insert into 'agent_service' table
INSERT INTO agent_service (service_id, agent_id) VALUES
         -- pam_jeanne_barret_B
         (2, 1), -- Christian Sauvage
         (2, 2), -- François Dambron
         (2, 3), -- Frédéric Grégoire
         (2, 4), -- Nicolas Delattre
         (2, 5), -- Jean François Albert
         (2, 6), -- Guillaume Gatoux
         (2, 7), -- Arnaud Celton
         (2, 8), -- Francis Beyaert
         (2, 9), -- Gaylord Suret
         (2, 10), -- Thierry Lesaulnier
         (2, 11), -- Laurent Duval
         (2, 12), -- Philippe Bon
         (2, 13), -- Adeline Lagadec
         (2, 14), -- Pascal Morel
         (2, 15), -- Yann Mollat

         -- pam_iris_A
         (5, 16), -- Xavier Lacourrege
         (5, 17), -- Thierry Crochard
         (5, 18), -- Marc Ottini
         (5, 19), -- Gilles Menuge
         (5, 20), -- Jean-David Duhaudt
         (5, 21), -- Stephane Bistour
         (5, 22), -- Herve Simon
         (5, 23), -- Fabien Roman
         (5, 24), -- Stéphane Lelièvre
         (5, 25), -- Yves Maniette
         (5, 26), -- Gaétan Merrien
         (5, 27), -- Frédéric Beyaert
         (5, 28), -- Franck Touron
         (5, 29), -- Frédéric Dechaine
         (5, 30), -- Francois Le Bon

         -- pam_themis_A
         (3, 31), -- Ariane Regaud
         (3, 32), -- Philippe Gahinet
         (3, 33), -- Didier Cozic
         (3, 34), -- Régis Soubise
         (3, 35), -- Philippe Davies
         (3, 36), -- Franck Grouiec
         (3, 37), -- Stéphane Hellio
         (3, 38), -- Pascal Rousselet
         (3, 39), -- Laurent Forgeard
         (3, 40), -- Oswald Isore
         (3, 41), -- Yvan Pavie
         (3, 42), -- Thierry Yhuel
         (3, 43), -- Stéphane Horel
         (3, 44), -- Pascal Barinka
         (3, 45), -- Nicolas Daden
         (3, 46), -- Cyril Le Scoarnec

         -- pam_themis_B
         (4, 47), -- Frédéric Schneider
         (4, 48), -- Pascal Isore
         (4, 49), -- Philippe Fournier
         (4, 50), -- Régis Tertu
         (4, 51), -- Raymond Cacitti
         (4, 52), -- Philippe Darsu
         (4, 53), -- Michel Marchand
         (4, 54), -- Cyril Lelandois
         (4, 55), -- Jean Marc Bourbigot
         (4, 56), -- David Collomp
         (4, 57), -- Jérome Fievet
         (4, 58), -- Brian O’Rorke
         (4, 59), -- Ludovic Daoulas
         (4, 60), -- Loup Genty
         (4, 61), -- JL Chapovaloff

        -- pam_gyptis_A
        (7, 62), -- Ludovic Bouteillon
        (7, 63), -- Serge Croville
        (7, 64), -- Sylvain Rebeyrotte
        (7, 65), -- Thomas Le Gall
        (7, 66), -- Lilian Roue
        (7, 67), -- Laurent Paronneau
        (7, 68), -- Nicolas Peyre
        (7, 69), -- José Maillot
        (7, 70), -- Michel Ceres
        (7, 71), -- David Demilly
        (7, 72), -- Anne Zamaron
        (7, 73), -- Cyrille Carval
        (7, 74), -- Guillaume Mardelle
        (7, 75); -- Pierre Buriez

END $$;