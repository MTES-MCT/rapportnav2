DO
$$
  BEGIN
    ALTER TABLE mission_crew
    ALTER COLUMN agent_role_id DROP NOT NULL;
  END
$$;
