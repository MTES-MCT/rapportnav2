-- PAM usage metrics (distance, GO, fuel), stored on the row of the PAM ship's MonitorEnv resource (unified with the
-- ULAM per-resource values). The PAM costs belong to the mission, not a resource: they stay in mission_general_info only.
-- Phase 1: written alongside mission_general_info, which stays the read source until the switch.
ALTER TABLE IF EXISTS public.mission_resource_usage
    ADD COLUMN IF NOT EXISTS distance_in_nautical_miles DOUBLE PRECISION NULL DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS consumed_go_in_liters      DOUBLE PRECISION NULL DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS consumed_fuel_in_liters    DOUBLE PRECISION NULL DEFAULT NULL;
