-- Consolidated migration (squash of three previous files):
--   * V1.2026.06.30 delete_duplicate_target2_external_id
--   * V1.2026.07.01 unique_constraint_general_info_mission_id
--   * V1.2026.07.14 add_index_mission_action_owner_id
-- All index creations use IF NOT EXISTS so this file is safe to re-run.

-- ==========================================================================
-- 1. target_2: drop duplicate rows sharing an external_id, keep the oldest,
--    then enforce uniqueness. Cascades through control_2 / infraction_2 /
--    infraction_natinf_2 for the deleted targets.
-- ==========================================================================
BEGIN;

WITH ranked AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY external_id
               ORDER BY created_at ASC NULLS LAST, id ASC
           ) AS rn
    FROM target_2
    WHERE external_id IS NOT NULL
),
to_delete AS (
    SELECT id FROM ranked WHERE rn > 1
),
controls_to_delete AS (
    SELECT c.id AS control_id
    FROM control_2 c
    WHERE c.target_id IN (SELECT id FROM to_delete)
),
deleted_natinf AS (
    DELETE FROM infraction_natinf_2
    WHERE infraction_id IN (
        SELECT i.id FROM infraction_2 i
        WHERE i.control_id IN (SELECT control_id FROM controls_to_delete)
    )
    RETURNING infraction_id
),
deleted_infractions AS (
    DELETE FROM infraction_2
    WHERE control_id IN (SELECT control_id FROM controls_to_delete)
    RETURNING id
),
deleted_controls AS (
    DELETE FROM control_2
    WHERE target_id IN (SELECT id FROM to_delete)
    RETURNING target_id
)
DELETE FROM target_2
WHERE id IN (SELECT id FROM to_delete);

CREATE UNIQUE INDEX IF NOT EXISTS idx_target2_external_id_unique
    ON target_2 (external_id)
    WHERE external_id IS NOT NULL;

COMMIT;

-- ==========================================================================
-- 2. mission_general_info: drop duplicate rows per mission_id, keep the
--    lowest id, then enforce one general_info per mission.
-- ==========================================================================
DELETE FROM mission_general_info
WHERE id NOT IN (
    SELECT MIN(id)
    FROM mission_general_info
    WHERE mission_id IS NOT NULL
    GROUP BY mission_id
)
AND mission_id IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_mission_general_info_mission_id_unique
    ON mission_general_info (mission_id)
    WHERE mission_id IS NOT NULL;

-- ==========================================================================
-- 3. mission_action: index the parent id (owner_id) used by findAllByOwnerId
--    on every mission load.
-- ==========================================================================
CREATE INDEX IF NOT EXISTS idx_mission_action_owner_id ON mission_action(owner_id);
