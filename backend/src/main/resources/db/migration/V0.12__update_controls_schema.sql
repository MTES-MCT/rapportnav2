DO $$
BEGIN
    ALTER TABLE control_gens_de_mer
    ADD COLUMN observations text;
    
    ALTER TABLE control_administrative_vessel
    ADD COLUMN observations text;
END $$;
