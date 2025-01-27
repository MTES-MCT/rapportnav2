DO
$$
  BEGIN

  INSERT INTO user_role (user_id, role)
  SELECT id, 'ADMIN'
  FROM "user"
  WHERE email = 'louis.hache@beta.gouv.fr';

  INSERT INTO user_role (user_id, role)
  SELECT id, 'ADMIN'
  FROM "user"
  WHERE email = 'christian.tonye@beta.gouv.fr';

  INSERT INTO user_role (user_id, role)
  SELECT id, 'ADMIN'
  FROM "user"
  WHERE email = 'aleck.vincent@beta.gouv.fr';

  INSERT INTO user_role (user_id, role)
  SELECT id, 'ADMIN'
  FROM "user"
  WHERE email = 'camille.nguyen@mer.gouv.fr';

  INSERT INTO user_role (user_id, role)
  SELECT id, 'ADMIN'
  FROM "user"
  WHERE email = 'clemence.buffeteau@atelier-universel.com';

  END
$$;
