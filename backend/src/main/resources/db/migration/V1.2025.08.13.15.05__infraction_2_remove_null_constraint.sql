DO
$$
  BEGIN
    ALTER TABLE infraction_2 ALTER COLUMN infraction_type SET DEFAULT 'WITHOUT_REPORT';
  END
$$;
