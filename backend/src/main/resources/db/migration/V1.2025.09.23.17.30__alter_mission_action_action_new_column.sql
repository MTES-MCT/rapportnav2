DO
$$
BEGIN
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS siren VARCHAR (36) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS control_type VARCHAR (36) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS nbr_of_Control INTEGER NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS sector_type VARCHAR (36) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS nbr_of_Control_amp INTEGER NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS nbr_of_Control_300m INTEGER NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS leisure_type VARCHAR (36) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS fishing_gear_type VARCHAR (36) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS is_control_during_security_day BOOLEAN NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS is_seizure_sleeping_fishing_gear BOOLEAN NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS sector_establishment_type VARCHAR (36) NULL;
END
$$;
