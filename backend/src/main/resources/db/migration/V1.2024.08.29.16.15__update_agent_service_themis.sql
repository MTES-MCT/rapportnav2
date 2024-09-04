DO
$$
BEGIN
    DELETE FROM agent_Service WHERE agent_id in (37, 48); -- delete Stéphane Hellio, Chef de quart (Themis A), Pascal Isore, Second (Themis B)
    INSERT INTO agent_service (service_id, agent_id, agent_role_id) VALUES (4, 37, 12); -- Stéphane Hellio second pam_themis_B
END $$;
