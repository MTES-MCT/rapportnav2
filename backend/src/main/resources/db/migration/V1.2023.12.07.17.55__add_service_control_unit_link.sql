DO $$
BEGIN
    INSERT INTO service_control_unit (service_id, control_unit_id) VALUES
         -- pam_jeanne_barret
         (1, 10121),
         (2, 10121),
         -- pam_themis
         (3, 10080),
         (4, 10080),
         -- pam_iris
         (5, 10404),
         (6, 10404),
         -- pam_gyptis
         (7, 10141),
         (8, 10141);
END $$;