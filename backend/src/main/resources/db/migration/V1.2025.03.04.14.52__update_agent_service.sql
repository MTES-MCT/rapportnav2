
-- Description: Add disabled_at column to agent_service table with time zone
ALTER TABLE agent_service
ADD COLUMN disabled_at TIMESTAMP WITH TIME ZONE DEFAULT NULL;

-- update Gyptis A
INSERT INTO agent (first_name, last_name)
VALUES ('Joel', 'Foret');

INSERT INTO agent_service (agent_id, service_id, agent_role_id)
SELECT a.id, 7, 15
FROM agent a
WHERE a.last_name LIKE '%Foret%';

UPDATE agent_service
SET disabled_at = NOW()
WHERE agent_id = (SELECT id FROM agent WHERE last_name = 'Rebeyrotte');

UPDATE agent_service
SET disabled_at = NOW()
WHERE agent_id = (SELECT id FROM agent WHERE last_name = 'Demilly');








