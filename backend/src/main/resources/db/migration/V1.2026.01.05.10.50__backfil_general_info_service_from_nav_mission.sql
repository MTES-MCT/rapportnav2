DO
$$
  BEGIN
    UPDATE public.mission_general_info mgi
    SET service_id = m.service_id
    FROM public.mission m
    WHERE
      mgi.mission_id_uuid IS NOT NULL
      AND mgi.service_id IS NULL
      AND m.id = mgi.mission_id_uuid;

  END
$$;
