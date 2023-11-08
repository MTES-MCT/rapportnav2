ALTER TABLE service
ADD COLUMN service_linked_id INT DEFAULT NULL
ADD CONSTRAINT fk_service_service_linked
FOREIGN KEY (service_linked_id) REFERENCES service (id)
ON DELETE SET NULL;
