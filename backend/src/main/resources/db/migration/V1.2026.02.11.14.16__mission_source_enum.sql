DO
$$
BEGIN
  UPDATE mission SET mission_source = 'RAPPORT_NAV' WHERE mission_source = 'RAPPORTNAV';
END
$$;
