DO
$$
  BEGIN
    UPDATE agent
    SET first_name = 'Pascal',
        last_name  = 'Isore'
    WHERE first_name = 'Stéphane'
      AND last_name = 'Hellio';

    UPDATE agent
    SET first_name = 'Loup',
        last_name  = 'Genty'
    WHERE first_name = 'Pascal'
      AND last_name = 'Barincka';

  END
$$;
