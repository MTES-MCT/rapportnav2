ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS port_locode character varying(16) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS zip_code character varying(16) NULL;
ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS city character varying(255) NULL;
