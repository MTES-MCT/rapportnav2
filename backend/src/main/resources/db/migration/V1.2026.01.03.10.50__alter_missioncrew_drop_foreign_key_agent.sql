DO
$$
  BEGIN
    ALTER TABLE mission_crew
    DROP CONSTRAINT mission_crew_agent_id_fkey;
  END
$$;
