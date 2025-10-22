BEGIN;

WITH duplicates AS (
  SELECT id, action_id, source, created_at,
         ROW_NUMBER() OVER (PARTITION BY action_id ORDER BY created_at DESC) AS rn
  FROM target_2
  WHERE source = 'RAPPORTNAV'
),
     to_delete AS (
       SELECT id, action_id
       FROM duplicates
       WHERE rn = 1  -- delete the most recent duplicate per action_id
         AND action_id IN (
         SELECT action_id
         FROM target_2
         GROUP BY action_id
         HAVING COUNT(*) > 1
       )
     ),
     deleted_control AS (
       DELETE FROM control_2
         WHERE target_id IN (SELECT id FROM to_delete)
         RETURNING target_id
     )
DELETE FROM target_2
WHERE id IN (SELECT id FROM to_delete);

COMMIT;
