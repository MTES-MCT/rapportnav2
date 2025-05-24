DO
$$
BEGIN

CREATE TABLE
  cross_control
(
  id                 uuid NOT NULL,
  type               character varying(32) NULL,
  status             character varying(32) NULL,
  origin             character varying(32) NULL,
  start_datetime_utc timestamp with time zone NULL,
  end_datetime_utc   timestamp with time zone NULL,
  vessel_id          integer NULL,
  agent_id           VARCHAR(36) NULL,
  service_id         integer NULL,
  conclusion         character varying(32) NULL
);

ALTER TABLE
  cross_control
  ADD
    CONSTRAINT cross_control_pkey PRIMARY KEY (id);
END
$$;
