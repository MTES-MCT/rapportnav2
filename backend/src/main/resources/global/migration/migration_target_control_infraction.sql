ROLLBACK;

DO $$
DECLARE
    my_rec RECORD;
    new_target_id uuid;
    target_insert_count integer := 0;
    control_insert_count integer := 0;
    infraction_insert_count integer := 0;
    infraction_natinf_insert_count integer := 0;
    my_infraction_natinf_curs CURSOR FOR
        SELECT
            infna.infraction_id,
        infna.natinf_code
        FROM infraction_natinf infna;
    my_control_curs CURSOR FOR
        SELECT cont.*, infra.*
        FROM (
          SELECT cgm.action_control_id as action_id,
                 cgm.id as control_id,
                 'GENS_DE_MER'::"ControlType" as control_type,
                 cgm.observations as control_observations,
                 cgm.amount_of_controls,
                 cgm.has_been_done,
                 cgm.staff_outnumbered,
                 cgm.up_to_date_medical_check,
                 cgm.knowledge_of_french_law_and_language,
                 null as compliant_operating_permit,
                 null as up_to_date_navigation_permit,
                 null as compliant_security_documents
          FROM control_gens_de_mer cgm
          UNION ALL SELECT cad.action_control_id as action_id,
                           cad.id as control_id,
                           'ADMINISTRATIVE'::"ControlType" as control_type,
                           cad.observations as control_observations,
                           cad.amount_of_controls,
                           cad.has_been_done,
                           null as staff_outnumbered,
                           null as up_to_date_medical_check,
                           null as knowledge_of_french_law_and_language,
                           cad.compliant_operating_permit,
                           cad.up_to_date_navigation_permit,
                           cad.compliant_security_documents
          FROM control_administrative cad
          UNION ALL SELECT cn.action_control_id as action_id,
                           cn.id as control_id,
                           'NAVIGATION'::"ControlType" as control_type,
                           cn.observations as control_observations,
                           cn.amount_of_controls,
                           cn.has_been_done,
                           null as staff_outnumbered,
                           null as up_to_date_medical_check,
                           null as knowledge_of_french_law_and_language,
                           null as compliant_operating_permit,
                           null as up_to_date_navigation_permit,
                           null as compliant_security_documents
          FROM control_navigation cn
          UNION ALL SELECT cs.action_control_id as action_id,
                           cs.id as control_id,
                           'SECURITY'::"ControlType" as control_type,
                           cs.observations as control_observations,
                           cs.amount_of_controls,
                           cs.has_been_done,
                           null as staff_outnumbered,
                           null as up_to_date_medical_check,
                           null as knowledge_of_french_law_and_language,
                           null as compliant_operating_permit,
                           null as up_to_date_navigation_permit,
                           null as compliant_security_documents
          FROM control_security cs
        ) cont
        FULL JOIN (
           SELECT inf.id as infraction_id,
                 iet.id as target_Id,
                 iet.vessel_identifier,
                 iet.identity_controlled_person,
                 iet.vessel_type,
                 iet.vessel_size,
                 inf.control_id,
                 inf.observations as infraction_observations,
                 inf.infraction_type
          FROM infraction inf
          LEFT JOIN infraction_env_target iet on inf.id = iet.infraction_id
        ) infra ON infra.control_id = cont.control_id;
BEGIN
    OPEN my_control_curs;
    LOOP
        FETCH my_control_curs INTO my_rec;
        EXIT WHEN NOT FOUND;

        RAISE NOTICE 'Processing control_id: %', my_rec.control_id;

        -- TARGET LOGIC
        IF my_rec.target_id IS NULL THEN
            -- Check if action_id exists in target_2 with type 'default'
            IF NOT EXISTS (
                SELECT 1 FROM target_2 WHERE action_id = my_rec.action_id AND target_type = 'DEFAULT'
            ) THEN
                -- Insert new entry with type 'default' and source 'RAPPORTNAV'
                new_target_id := gen_random_uuid();
                RAISE NOTICE ' Insert new target_id: %', new_target_id;
                INSERT INTO target_2 (
                    id,
                    action_id,
                    target_type,
                    status,
                    source,
                    vessel_identifier,
                    identity_controlled_person,
                    vessel_type,
                    vessel_size
                )
                VALUES (
                    new_target_id,
                    my_rec.action_id,
                    'DEFAULT',
                    'IN_PROCESS',
                    'RAPPORTNAV',
                    my_rec.vessel_identifier,
                    my_rec.identity_controlled_person,
                    my_rec.vessel_size,
                    my_rec.vessel_size
                );
                target_insert_count := target_insert_count + 1;
            END IF;
        ELSE
            -- Check if no line with action_id and source 'MONITORENV'
            IF NOT EXISTS (
                SELECT 1 FROM target_2 WHERE action_id = my_rec.action_id AND source = 'MONITORENV'
            ) THEN
                -- Insert new entry with source 'MONITORENV'
                new_target_id := gen_random_uuid();
                RAISE NOTICE ' Insert ENV target_id: %', new_target_id;
                INSERT INTO target_2 (
                    id,
                    action_id,
                    target_type,
                    status,
                    source,
                    vessel_identifier,
                    identity_controlled_person,
                    vessel_type,
                    vessel_size
                )
                VALUES (
                    new_target_id,
                    my_rec.action_id,
                    'INDIVIDUAL',
                    NULL,
                    'MONITORENV',
                    my_rec.vessel_identifier,
                    my_rec.identity_controlled_person,
                    my_rec.vessel_size,
                    my_rec.vessel_size
                );
                target_insert_count := target_insert_count + 1;
            END IF;
        END IF;

        -- CONTROL LOGIC
        IF NOT EXISTS (
            SELECT 1 FROM control_2 WHERE id = my_rec.control_id
        ) THEN
            -- Insert new entry into control_2
             RAISE NOTICE 'Insert new control, control_id %', my_rec.control_id;
            INSERT INTO control_2 (
                id,
                control_type,
                observations,
                amount_of_controls,
                has_been_done,
                staff_outnumbered,
                up_to_date_medical_check,
                knowledge_of_french_law_and_language,
                compliant_operating_permit,
                up_to_date_navigation_permit,
                compliant_security_documents
            )
            VALUES (
                my_rec.control_id,
                my_rec.control_type,
                my_rec.control_observations,
                my_rec.amount_of_controls,
                my_rec.has_been_done,
                my_rec.staff_outnumbered,
                my_rec.up_to_date_medical_check,
                my_rec.knowledge_of_french_law_and_language,
                my_rec.compliant_operating_permit,
                my_rec.up_to_date_navigation_permit,
                my_rec.compliant_security_documents
            );
            control_insert_count := control_insert_count + 1;
        END IF;

        -- INFRACTION LOGIC
        IF my_rec.infraction_id IS NOT NULL THEN
            IF NOT EXISTS (
                SELECT 1 FROM infraction_2 WHERE id = my_rec.infraction_id
            ) THEN
            -- Insert new entry into infraction_2
             RAISE NOTICE 'Insert new infraction, infraction_id: %', my_rec.infraction_id;
            INSERT INTO infraction_2 (
                id,
                control_id,
                infraction_type,
                observations
            )
            VALUES (
                my_rec.infraction_id,
                my_rec.control_id,
                my_rec.infraction_type,
                my_rec.infraction_observations
            );
            infraction_insert_count := infraction_insert_count + 1;
            END IF;
        END IF;

    END LOOP;
    CLOSE my_control_curs;

    RAISE NOTICE 'Inserted % rows into target_2', target_insert_count;
    RAISE NOTICE 'Inserted % rows into control_2', control_insert_count;
    RAISE NOTICE 'Inserted % rows into infraction_2', infraction_insert_count;


    OPEN my_infraction_natinf_curs;
    LOOP
        FETCH my_infraction_natinf_curs INTO my_rec;
        EXIT WHEN NOT FOUND;

        IF NOT EXISTS (
                SELECT 1 FROM infraction_natinf_2 WHERE infraction_id = my_rec.infraction_id AND natinf_code = my_rec.natinf_code
            ) THEN
            -- Insert new entry into infraction_2
             RAISE NOTICE 'Insert new infraction natinf, infraction_id: %, natinf_code: %', my_rec.infraction_id, my_rec.natinf_code;
            INSERT INTO infraction_natinf_2 (
                infraction_id,
                natinf_code
            )
            VALUES (
                my_rec.infraction_id,
                my_rec.natinf_code
            );
            infraction_natinf_insert_count := infraction_natinf_insert_count + 1;
            END IF;

     END LOOP;
    CLOSE my_infraction_natinf_curs;

    RAISE NOTICE 'Inserted % rows into infraction_natinf_2', infraction_natinf_insert_count;
END
$$;