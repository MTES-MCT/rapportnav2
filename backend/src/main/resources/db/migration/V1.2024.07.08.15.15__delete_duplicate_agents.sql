DO
$$
  BEGIN

    -- delete duplicate agents
    DELETE
    from agent_service
    WHERE agent_id BETWEEN 93 AND 105;

    DELETE
    FROM agent
    WHERE id BETWEEN 93 AND 105;

  END
$$;
