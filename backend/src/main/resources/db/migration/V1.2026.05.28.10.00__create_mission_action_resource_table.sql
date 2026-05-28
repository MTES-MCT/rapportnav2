-- Create the collection table for mission_action resource IDs
CREATE TABLE IF NOT EXISTS mission_action_resource (
    action_id UUID NOT NULL,
    resource_id INTEGER NOT NULL,
    CONSTRAINT fk_mission_action_resource_action FOREIGN KEY (action_id) REFERENCES mission_action(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_mission_action_resource_action_id ON mission_action_resource(action_id);

-- Migrate existing resource_id data from mission_action to the new table
INSERT INTO mission_action_resource (action_id, resource_id)
SELECT id, resource_id
FROM mission_action
WHERE resource_id IS NOT NULL;

-- Drop the old column now that data has been migrated
ALTER TABLE mission_action DROP COLUMN IF EXISTS resource_id;
