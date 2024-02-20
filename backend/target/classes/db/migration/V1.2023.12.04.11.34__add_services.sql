DO $$
BEGIN
    INSERT INTO service (id, name, service_linked_id) VALUES
                                   (1, 'pam_jeanne_barret_A', 2),
                                   (2, 'pam_jeanne_barret_B', 1),
                                   (3, 'pam_themis_A', 4),
                                   (4, 'pam_themis_B', 3),
                                   (5, 'pam_iris_A', 6),
                                   (6, 'pam_iris_B', 5),
                                   (7, 'pam_gyptis_A', 8),
                                   (8, 'pam_gyptis_B', 7);

END $$;