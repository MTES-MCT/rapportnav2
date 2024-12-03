DO
$$
  BEGIN

ALTER TABLE control_administrative
  ADD COLUMN has_been_done BOOLEAN;

ALTER TABLE control_gens_de_mer
  ADD COLUMN has_been_done BOOLEAN;

ALTER TABLE control_navigation
  ADD COLUMN has_been_done BOOLEAN;

ALTER TABLE control_security
  ADD COLUMN has_been_done BOOLEAN;

  END
$$;
