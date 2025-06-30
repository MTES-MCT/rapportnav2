DO
$$
  BEGIN
    ALTER TABLE mission ADD CONSTRAINT mission_pkey PRIMARY KEY (id);
  END
$$;
