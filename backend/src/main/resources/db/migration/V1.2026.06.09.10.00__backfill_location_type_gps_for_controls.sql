UPDATE mission_action
SET location_type = 'GPS'
WHERE action_type = 'CONTROL'
  AND latitude IS NOT NULL
  AND longitude IS NOT NULL
  AND location_type IS NULL;