DO
$$
  BEGIN
    DELETE FROM mission_general_info
    WHERE id NOT IN (
      SELECT MIN(id)
      FROM mission_general_info
      WHERE mission_id IS NOT NULL
      GROUP BY mission_id
    )
      AND mission_id IS NOT NULL;
  END
$$;
