DO
$$
  BEGIN

    -- insert test ULAM service
    INSERT INTO service (id, name, service_linked_id)
    VALUES (9, 'ulam_33', null);

    INSERT INTO service_control_unit (service_id, control_unit_id)
    VALUES (9, 10225);


  END
$$;
