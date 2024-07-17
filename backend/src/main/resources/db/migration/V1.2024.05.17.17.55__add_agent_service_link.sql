DO $$
BEGIN
    -- Insert into 'agent_service' table
INSERT INTO agent_service (service_id, agent_id) VALUES
         -- pam_themis_A
         (3, 92),
         -- pam_themis_B
         (4, 93),
         (4, 94),
         (4, 95),
         (4, 96),
         (4, 97),
         (4, 98),
         (4, 99),
         (4, 100),
         (4, 101),
         (4, 102),
         (4, 103),
         (4, 104),
         (4, 105);
END $$;
