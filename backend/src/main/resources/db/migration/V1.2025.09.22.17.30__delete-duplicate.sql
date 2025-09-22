DO
$$
  BEGIN
    DELETE FROM mission_general_info
    WHERE ctid = (
      SELECT ctid
      FROM mission_general_info
      WHERE mission_id = 11804
      ORDER BY ctid DESC
      LIMIT 1
    );
  END
$$;
