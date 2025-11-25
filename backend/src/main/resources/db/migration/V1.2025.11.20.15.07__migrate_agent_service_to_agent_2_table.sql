ROLLBACK;

DO
$$
  DECLARE
    my_rec                    RECORD;
    agent_insert_count        integer := 0;
    control_unit_id_set_count integer := 0;
    my_control_unit_curs CURSOR FOR
      SELECT se.id,
             scu.control_unit_id
      FROM service se
             INNER JOIN service_control_unit scu
                        ON scu.service_id = se.id;
    my_agent_service_curs CURSOR FOR
      SELECT ag.id as id,
             ag.first_name,
             ag.last_name,
             u.id as user_id,
             ags.service_id,
             ags.agent_role_id,
             ags.disabled_at,
             ags.created_at,
             ags.updated_at,
             ags.created_by,
             ags.updated_by,
             lower(concat(trim(ag.first_name), trim(ag.last_name))) as full_name
      FROM agent_service ags
             RIGHT JOIN agent ag on ag.id = ags.agent_id
             LEFT JOIN "user" u on lower(concat(trim(u.first_name), trim(u.last_name))) LIKE
                                   lower(concat(trim(ag.first_name), trim(ag.last_name)))
      WHERE ags.service_id is not null
      ORDER by ag.id asc;
  BEGIN
    OPEN my_agent_service_curs;
    LOOP
      FETCH my_agent_service_curs INTO my_rec;
      EXIT WHEN NOT FOUND;

      RAISE NOTICE 'Processing agent: %', my_rec.id;

      -- AGENT LOGIC
      IF NOT EXISTS (SELECT 1
                     FROM agent_2 a
                     WHERE lower(concat(trim(a.first_name), trim(a.last_name))) = my_rec.full_name
                       AND service_id = my_rec.service_id) THEN
        RAISE NOTICE 'Insert new agent, full name: %, on service_id: %', my_rec.full_name, my_rec.service_id;

        INSERT INTO agent_2 (first_name,
                             last_name,
                             user_id,
                             service_id,
                             agent_role_id,
                             disabled_at,
                             created_at,
                             updated_at,
                             created_by,
                             updated_by)
        VALUES (my_rec.first_name,
                my_rec.last_name,
                my_rec.user_id,
                my_rec.service_id,
                my_rec.agent_role_id,
                my_rec.disabled_at,
                my_rec.created_at,
                my_rec.updated_at,
                my_rec.created_by,
                my_rec.updated_by);
        agent_insert_count := agent_insert_count + 1;
      END IF;

    END LOOP;
    CLOSE my_agent_service_curs;

    RAISE NOTICE 'Inserted % rows into agent_2', agent_insert_count;

    OPEN my_control_unit_curs;
    LOOP
      FETCH my_control_unit_curs INTO my_rec;
      EXIT WHEN NOT FOUND;

      IF NOT EXISTS (SELECT 1 FROM service WHERE id = my_rec.id AND control_unit_id = my_rec.control_unit_id) THEN
        RAISE NOTICE 'set service, service_id: %, with control_unit_id: %', my_rec.id, my_rec.control_unit_id;
        UPDATE service
        SET control_unit_id = my_rec.control_unit_id
        WHERE id = my_rec.id;
        control_unit_id_set_count := control_unit_id_set_count + 1;
      END IF;

    END LOOP;
    CLOSE my_control_unit_curs;

    RAISE NOTICE 'Set % rows into service', control_unit_id_set_count;
  END;
$$
