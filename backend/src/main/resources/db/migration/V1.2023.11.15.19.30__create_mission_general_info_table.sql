DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_general_info') THEN
        CREATE TABLE mission_general_info (
            id INT PRIMARY KEY NOT NULL,
            mission_id INT NOT NULL,
            distance_in_nautical_miles FLOAT,
            consumed_go_in_liters FLOAT,
            consumed_fuel_in_liters FLOAT
        );
    END IF;
END $$;
