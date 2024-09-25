-- V3__update_multiple_agents.sql

DO
$$
  BEGIN
    UPDATE agent
    SET first_name = 'Guillaume',
        last_name  = 'Langlois'
    WHERE first_name = 'Dominique'
      AND last_name = 'Maingraud';

    WITH first_fabien_roman AS (SELECT id
                                FROM agent
                                WHERE first_name = 'Fabien'
                                  AND last_name = 'Roman'
                                ORDER BY id
                                LIMIT 1)
    UPDATE agent
    SET first_name = 'Marius',
        last_name  = 'Bruneau'
    WHERE id = (SELECT id FROM first_fabien_roman);

    WITH first_herve_simon AS (SELECT id
                               FROM agent
                               WHERE first_name = 'Herve'
                                 AND last_name = 'Simon'
                               ORDER BY id
                               LIMIT 1)
    UPDATE agent
    SET first_name = 'Hugues',
        last_name  = 'Willocq'
    WHERE id = (SELECT id FROM first_herve_simon);

  END
$$;
