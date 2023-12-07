DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'service_control_unit') THEN
        CREATE TABLE service_control_unit (
           service_id INT,
           control_unit_id INT,
           FOREIGN KEY (service_id) REFERENCES service(id),
           PRIMARY KEY (service_id, control_unit_id)
        );
    END IF;
END $$;