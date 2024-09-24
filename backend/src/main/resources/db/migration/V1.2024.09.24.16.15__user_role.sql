CREATE TABLE user_role
(
  user_id INT        NOT NULL,
  role    "RoleType" NOT NULL,
  CONSTRAINT pk_user_role PRIMARY KEY (user_id, role),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
      REFERENCES public."user" (id)
      ON DELETE CASCADE
);

INSERT INTO user_role (user_id, role)
SELECT id, unnest(roles) AS role
FROM "user"
WHERE roles IS NOT NULL;

ALTER TABLE "user"
  DROP COLUMN roles;


