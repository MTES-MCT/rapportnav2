DO
$$
  BEGIN

    -- insert test ULAM service
    INSERT INTO service (id, name, service_linked_id)
    VALUES (12, 'ULAM 974', null);

    INSERT INTO service_control_unit (service_id, control_unit_id)
    VALUES (12, 10183);


  END
$$;
