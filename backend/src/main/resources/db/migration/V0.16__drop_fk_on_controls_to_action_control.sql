DO $$
BEGIN
    ALTER TABLE control_gens_de_mer
    DROP CONSTRAINT fk_control_gens_de_mer_action_id;

    ALTER TABLE control_security
    DROP CONSTRAINT fk_control_security_action_id;

    ALTER TABLE control_navigation
    DROP CONSTRAINT fk_control_navigation_action_id;

    ALTER TABLE control_administrative
    DROP CONSTRAINT fk_control_administrative_action_id;
END $$;
