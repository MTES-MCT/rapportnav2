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

CREATE UNIQUE INDEX idx_target2_external_id_unique
    ON target_2 (external_id)
    WHERE external_id IS NOT NULL;

COMMIT;
