DO
$$
BEGIN

  ALTER TABLE mission_general_info
    ADD COLUMN IF NOT EXISTS operating_costs_in_euro REAL;

  ALTER TABLE mission_general_info
    ADD COLUMN IF NOT EXISTS fuel_costs_in_euro REAL;

END
$$;

