DO
$$
BEGIN
    -- Remove data related to missions 621 and 622

    -- Delete from infraction tables
    DELETE
    FROM infraction_natinf;

    DELETE
    FROM infraction_env_target
    WHERE mission_id IN (621, 622);

    DELETE
    FROM infraction
    WHERE mission_id IN (621, 622);

    -- Delete from control_administrative table
    DELETE
    FROM control_administrative
    WHERE mission_id IN (621, 622);

    -- Delete from control_security table
    DELETE
    FROM control_security
    WHERE mission_id IN (621, 622);

    -- Delete from control_navigation table
    DELETE
    FROM control_navigation
    WHERE mission_id IN (621, 622);

    -- Delete from control_gens_de_mer table
    DELETE
    FROM control_gens_de_mer
    WHERE mission_id IN (621, 622);

END $$;
