DO
$$
BEGIN

CREATE TABLE agent_2
(
  id            serial                 PRIMARY KEY,
  service_id    integer                NOT NULL,
  agent_role_id integer NULL,
  user_id       integer NULL,
  first_name    character varying(255) NOT NULL,
  last_name     character varying(255) NOT NULL,
  disabled_at   timestamp with time zone NULL,
  created_at    timestamp without time zone NULL DEFAULT now(),
  updated_at    timestamp without time zone NULL,
  created_by    integer NULL,
  updated_by    integer NULL,
  FOREIGN KEY (service_id) REFERENCES service(id),
  FOREIGN KEY (agent_role_id) REFERENCES agent_role(id)
);

ALTER TABLE service ADD COLUMN IF NOT EXISTS control_unit_id INT;

END
$$;

