DO
$$
  BEGIN

    -- insert test ULAM service
    INSERT INTO service (id, name, service_linked_id)
    VALUES (10, 'ULAM 971', null);

    INSERT INTO service_control_unit (service_id, control_unit_id)
    VALUES (10, 10169);


  END
$$;
