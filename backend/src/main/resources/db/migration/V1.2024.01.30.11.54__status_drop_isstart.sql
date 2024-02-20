DO
$$
  BEGIN

    -- Remove the isStart column from the mission_action_status table
    ALTER TABLE mission_action_status
      DROP COLUMN is_start;

  END
$$;
