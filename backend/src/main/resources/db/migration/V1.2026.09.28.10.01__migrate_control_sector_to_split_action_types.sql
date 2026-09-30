
CREATE TABLE mission_action_control_sector_backup AS
SELECT id, action_type, sector_type, sector_establishment_type
FROM mission_action
WHERE action_type = 'CONTROL_SECTOR';

UPDATE mission_action
SET action_type = 'CONTROL_ROADSIDE'
WHERE action_type = 'CONTROL_SECTOR'
  AND sector_establishment_type = 'ROADSIDE_INSPECTION';

UPDATE mission_action
SET action_type = 'CONTROL_SECTOR_FISHING'
WHERE action_type = 'CONTROL_SECTOR'
  AND (sector_type = 'FISHING' OR sector_type IS NULL);

UPDATE mission_action
SET action_type = 'CONTROL_SECTOR_PLAISANCE'
WHERE action_type = 'CONTROL_SECTOR'
  AND sector_type = 'PLEASURE';

DO
$$
BEGIN
  IF EXISTS (SELECT 1 FROM mission_action WHERE action_type = 'CONTROL_SECTOR') THEN
    RAISE EXCEPTION 'CONTROL_SECTOR rows remain after migration, check sector_type values';
  END IF;
END $$;

ALTER TABLE mission_action
  ADD CONSTRAINT mission_action_no_legacy_control_sector CHECK (action_type <> 'CONTROL_SECTOR');
