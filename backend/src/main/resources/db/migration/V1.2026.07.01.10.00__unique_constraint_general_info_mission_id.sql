DELETE FROM mission_general_info
WHERE id NOT IN (
    SELECT MIN(id)
    FROM mission_general_info
    WHERE mission_id IS NOT NULL
    GROUP BY mission_id
)
AND mission_id IS NOT NULL;

CREATE UNIQUE INDEX idx_mission_general_info_mission_id_unique
    ON mission_general_info (mission_id)
    WHERE mission_id IS NOT NULL;
