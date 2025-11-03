DO
$$
BEGIN
PERFORM setval('service_id_seq', (SELECT MAX(id) FROM service));
END
$$;

