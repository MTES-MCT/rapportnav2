-- Rename service names
UPDATE service
SET name = CASE
             WHEN name = 'pam_jeanne_barret_A' THEN 'PAM Jeanne Barret A'
             WHEN name = 'pam_jeanne_barret_B' THEN 'PAM Jeanne Barret B'
             WHEN name = 'pam_themis_A' THEN 'PAM Themis A'
             WHEN name = 'pam_themis_B' THEN 'PAM Themis B'
             WHEN name = 'pam_iris_A' THEN 'PAM Iris A'
             WHEN name = 'pam_iris_B' THEN 'PAM Iris B'
             WHEN name = 'pam_gyptis_A' THEN 'PAM Gyptis A'
             WHEN name = 'pam_gyptis_B' THEN 'PAM Gyptis B'
             ELSE name
  END;
