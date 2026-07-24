-- Persist mission-level validation (completeness-for-stats) on the mission row so reads can trust it
-- instead of recomputing every action on the fly: a mission known-complete implies all its actions are
-- complete. Recomputed and written on any action create/update/delete; aggregated from the mission's
-- actions + env data + general info. Stored on the mission row (not a side table) since every mission has
-- exactly one row (nav = source, env = local mirror).
ALTER TABLE mission
    ADD COLUMN IF NOT EXISTS is_complete_for_stats BOOLEAN,
    ADD COLUMN IF NOT EXISTS sources_of_missing_data TEXT;
