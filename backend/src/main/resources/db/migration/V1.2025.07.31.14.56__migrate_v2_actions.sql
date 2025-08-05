DO
$$
  BEGIN
    -- step 1: import Action tables
    -- anti pollution
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats,
      latitude,
      longitude,
      detected_pollution,
      pollution_observed_by_authorized_agent,
      diversion_carried_out,
      simple_brewing_operation,
      anti_pol_device_deployed
    )
      (
        SELECT
          'ANTI_POLLUTION' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats,
          latitude,
          longitude,
          detected_pollution,
          pollution_observed_by_authorized_agent,
          diversion_carried_out,
          simple_brewing_operation,
          anti_pol_device_deployed
        FROM mission_action_anti_pollution pol
      )
    ON CONFLICT (id) DO NOTHING;

    -- baeem
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'BAAEM_PERMANENCE' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats

        FROM mission_action_baaem_permanence
      )
    ON CONFLICT (id) DO NOTHING;

    -- control
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats,
      latitude,
      longitude,
      control_method,
      vessel_identifier,
      vessel_type,
      vessel_size,
      identity_controlled_person
    )
      (
        SELECT
          'CONTROL' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats,
          latitude,
          longitude,
          control_method,
          vessel_identifier,
          vessel_type,
          vessel_size,
          identity_controlled_person
        FROM mission_action_control pol
      )
    ON CONFLICT (id) DO NOTHING;

    -- note
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'NOTE' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          observations,
          is_complete_for_stats

        FROM mission_action_free_note
      )
    ON CONFLICT (id) DO NOTHING;

    -- illegal immigration
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats,
      latitude,
      longitude,
      nb_of_intercepted_vessels,
      nb_of_intercepted_migrants,
      nb_of_suspected_smugglers
    )
      (
        SELECT
          'ILLEGAL_IMMIGRATION' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats,
          latitude,
          longitude,
          nb_of_intercepted_vessels,
          nb_of_intercepted_migrants,
          nb_of_suspected_smugglers
        FROM mission_action_illegal_immigration pol
      )
    ON CONFLICT (id) DO NOTHING;

    -- rescue
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats,
      latitude,
      longitude,
      is_vessel_rescue,
      is_person_rescue,
      is_vessel_noticed,
      is_vessel_towed,
      is_in_srr_or_followed_by_cross_mrcc,
      number_persons_rescued,
      number_of_deaths,
      operation_follows_defrep,
      location_description,
      is_migration_rescue,
      nb_vessels_tracked_without_intervention,
      nb_assisted_vessels_returning_to_shore
    )
      (
        SELECT
          'RESCUE' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats,
          latitude,
          longitude,
          is_vessel_rescue,
          is_person_rescue,
          is_vessel_noticed,
          is_vessel_towed,
          is_in_srr_or_followed_by_cross_mrcc,
          number_persons_rescued,
          number_of_deaths,
          operation_follows_defrep,
          location_description,
          is_migration_rescue,
          nb_vessels_tracked_without_intervention,
          nb_assisted_vessels_returning_to_shore
        FROM mission_action_rescue
      )
    ON CONFLICT (id) DO NOTHING;

    --nautical event
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'NAUTICAL_EVENT' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats
        FROM mission_action_nautical_event
      )
    ON CONFLICT (id) DO NOTHING;

    ---  public order
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'PUBLIC_ORDER' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats
        FROM mission_action_public_order
      )
    ON CONFLICT (id) DO NOTHING;

    --- representatoion
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'REPRESENTATION' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats
        FROM mission_action_representation
      )
    ON CONFLICT (id) DO NOTHING;


    --- status
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'STATUS' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          observations,
          is_complete_for_stats
        FROM mission_action_status
      )
    ON CONFLICT (id) DO NOTHING;

    --- vigimer
    INSERT INTO mission_action
    (
      action_type,
      id,
      mission_id,
      start_datetime_utc,
      end_datetime_utc,
      observations,
      is_complete_for_stats
    )
      (
        SELECT
          'VIGIMER' AS action_type,
          id,
          mission_id,
          start_datetime_utc,
          end_datetime_utc,
          observations,
          is_complete_for_stats
        FROM mission_action_vigimer
      )
    ON CONFLICT (id) DO NOTHING;



    -- Step 2a: Migrate Target data from mission_action_control (Action records become Targets)
    INSERT INTO target_2 (
      id,
      action_id,
      target_type,
      start_datetime_utc,
      end_datetime_utc,
      vessel_identifier,
      vessel_type,
      vessel_size,
      identity_controlled_person
    )
    SELECT
      mac.id,
      mac.id as action_id, -- The action becomes its own target
      'DEFAULT'::"TargetType",
      mac.start_datetime_utc,
      mac.end_datetime_utc,
      mac.vessel_identifier,
      mac.vessel_type,
      mac.vessel_size,
      mac.identity_controlled_person
    FROM mission_action_control mac
    WHERE NOT EXISTS (
      SELECT 1 FROM target_2 t2 WHERE t2.id = mac.id
    );

    -- Step 2b: Migrate Target data from infraction_env_target (existing targets with infractions)
    INSERT INTO target_2 (
      id,
      action_id,
      target_type,
      vessel_identifier,
      vessel_type,
      vessel_size,
      identity_controlled_person
    )
    SELECT DISTINCT
      iet.id,
      iet.action_id,
      'DEFAULT'::"TargetType",
      iet.vessel_identifier,
      iet.vessel_type,
      iet.vessel_size,
      iet.identity_controlled_person
    FROM infraction_env_target iet
    WHERE NOT EXISTS (
      SELECT 1 FROM target_2 t2 WHERE t2.id = iet.id
    );

    -- step 2c: import fish targets
    -- Step 1c: Create targets for controls with non-UUID action_control_id
    INSERT INTO target_2 (
      id,
      action_id,
      target_type
    )
    SELECT DISTINCT
      gen_random_uuid() as id,
      c.action_control_id as action_id,
      'DEFAULT'::"TargetType" as target_type
    FROM (
           SELECT action_control_id FROM control_administrative WHERE action_control_id IS NOT NULL AND action_control_id !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
           UNION
           SELECT action_control_id FROM control_gens_de_mer WHERE action_control_id IS NOT NULL AND action_control_id !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
           UNION
           SELECT action_control_id FROM control_navigation WHERE action_control_id IS NOT NULL AND action_control_id !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
           UNION
           SELECT action_control_id FROM control_security WHERE action_control_id IS NOT NULL AND action_control_id !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         ) c
    WHERE NOT EXISTS (
      SELECT 1 FROM target_2 t2 WHERE t2.action_id = c.action_control_id
    );


    -- Step 3: Migrate Control data
    -- For control_gens_de_mer (via infraction path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      staff_outnumbered,
      up_to_date_medical_check,
      knowledge_of_french_law_and_language,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cgm.id,
      'GENS_DE_MER'::"ControlType",
      iet.id as target_id,
      cgm.observations,
      cgm.staff_outnumbered,
      cgm.up_to_date_medical_check,
      cgm.knowledge_of_french_law_and_language,
      cgm.amount_of_controls,
      cgm.has_been_done
    FROM control_gens_de_mer cgm
           JOIN infraction i ON i.control_id = cgm.id AND i.control_type = 'GENS_DE_MER'
           JOIN infraction_env_target iet ON iet.infraction_id = i.id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cgm.id
    );

    -- For control_gens_de_mer (via mission_action_control path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      staff_outnumbered,
      up_to_date_medical_check,
      knowledge_of_french_law_and_language,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cgm.id,
      'GENS_DE_MER'::"ControlType",
      t2.id as target_id,
      cgm.observations,
      cgm.staff_outnumbered,
      cgm.up_to_date_medical_check,
      cgm.knowledge_of_french_law_and_language,
      cgm.amount_of_controls,
      cgm.has_been_done
    FROM control_gens_de_mer cgm
           JOIN target_2 t2 ON t2.action_id = cgm.action_control_id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cgm.id
    );

    -- For control_navigation (via infraction path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cn.id,
      'NAVIGATION'::"ControlType",
      iet.id as target_id,
      cn.observations,
      cn.amount_of_controls,
      cn.has_been_done
    FROM control_navigation cn
           JOIN infraction i ON i.control_id = cn.id AND i.control_type = 'NAVIGATION'
           JOIN infraction_env_target iet ON iet.infraction_id = i.id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cn.id
    );

    -- For control_navigation (via mission_action_control path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cn.id,
      'NAVIGATION'::"ControlType",
      t2.id as target_id,
      cn.observations,
      cn.amount_of_controls,
      cn.has_been_done
    FROM control_navigation cn
           JOIN target_2 t2 ON t2.action_id = cn.action_control_id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cn.id
    );

    -- For control_security (via infraction path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cs.id,
      'SECURITY'::"ControlType",
      iet.id as target_id,
      cs.observations,
      cs.amount_of_controls,
      cs.has_been_done
    FROM control_security cs
           JOIN infraction i ON i.control_id = cs.id AND i.control_type = 'SECURITY'
           JOIN infraction_env_target iet ON iet.infraction_id = i.id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cs.id
    );

    -- For control_security (via mission_action_control path)
    INSERT INTO control_2 (
      id,
      control_type,
      target_id,
      observations,
      amount_of_controls,
      has_been_done
    )
    SELECT
      cs.id,
      'SECURITY'::"ControlType",
      t2.id as target_id,
      cs.observations,
      cs.amount_of_controls,
      cs.has_been_done
    FROM control_security cs
           JOIN target_2 t2 ON t2.action_id = cs.action_control_id
    WHERE NOT EXISTS (
      SELECT 1 FROM control_2 c2 WHERE c2.id = cs.id
    );


    -- Step 4: Migrate Infraction data
    INSERT INTO infraction_2 (
      id,
      control_id,
      infraction_type,
      observations
    )
    SELECT
      i.id,
      i.control_id,
      COALESCE(i.infraction_type, 'WITHOUT_REPORT') as infraction_type,
      i.observations
    FROM infraction i
    WHERE NOT EXISTS (
      SELECT 1 FROM infraction_2 i2 WHERE i2.id = i.id
    );

    -- Step 5: Migrate Natinf data
    INSERT INTO infraction_natinf_2 (
      infraction_id,
      natinf_code
    )
    SELECT
      inn.infraction_id,
      inn.natinf_code
    FROM infraction_natinf inn
    WHERE NOT EXISTS (
      SELECT 1 FROM infraction_natinf_2 inn2
      WHERE inn2.infraction_id = inn.infraction_id
        AND inn2.natinf_code = inn.natinf_code
    );



  END
$$;
