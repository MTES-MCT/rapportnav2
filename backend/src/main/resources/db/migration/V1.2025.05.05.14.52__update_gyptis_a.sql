UPDATE agent_service
SET disabled_at = NOW()
WHERE agent_id = (SELECT id FROM agent WHERE last_name = 'Ceres');








