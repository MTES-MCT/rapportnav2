DO
$$
BEGIN

CREATE
  TYPE "InfractionType"
  AS ENUM (
    'WAITING',
    'WITH_REPORT',
    'WITHOUT_REPORT');


CREATE TABLE
  infraction_2
(
  id              uuid             NOT NULL,
  control_id      uuid             NOT NULL,
  infraction_type "InfractionType" NOT NULL,
  observations    text NULL
);

ALTER TABLE
  infraction_2
  ADD
    CONSTRAINT infraction_2_pkey PRIMARY KEY (id);


CREATE
  TYPE "ControlType"
  AS ENUM (
    'ADMINISTRATIVE',
    'GENS_DE_MER',
    'NAVIGATION',
    'SECURITY');


CREATE TABLE
  control_2
(
  id                                   uuid          NOT NULL,
  control_type                         "ControlType" NOT NULL,
  target_id                            uuid          NOT NULL,
  observations                         text NULL,
  compliant_operating_permit           character varying(16) NULL,
  up_to_date_navigation_permit         character varying(16) NULL,
  compliant_security_documents         character varying(16) NULL,
  unit_should_confirm                  boolean NULL,
  unit_has_confirmed                   boolean NULL,
  amount_of_controls                   integer       NOT NULL DEFAULT 1,
  staff_outnumbered                    character varying(16) NULL,
  up_to_date_medical_check             character varying(16) NULL,
  knowledge_of_french_law_and_language character varying(16) NULL,
  has_been_done                        boolean NULL,
  nbr_of_hours                         integer NULL
);

ALTER TABLE
  control_2
  ADD
    CONSTRAINT control_2_pkey PRIMARY KEY (id);

ALTER TABLE control_2
  ADD UNIQUE (control_type, target_id);


CREATE
  TYPE "TargetType"
  AS ENUM (
    'VEHICLE',
    'COMPANY',
    'INDIVIDUAL');


CREATE TABLE
  target_2
(
  id                         uuid                   NOT NULL,
  action_id                  uuid                   NOT NULL,
  target_type                "TargetType"           NOT NULL,
  start_datetime_utc         timestamp with time zone NULL,
  end_datetime_utc           timestamp with time zone NULL,
  status                     character varying(32) NULL,
  source                     character varying(32) NULL,
  main_agent                 character varying(128) NULL,
  vessel_name                character varying(32) NULL,
  vessel_identifier          character varying(64) NULL,
  vessel_type                character varying(32) NULL,
  vessel_size                character varying(32) NULL,
  identity_controlled_person character varying(128) NOT NULL
);

ALTER TABLE
  target_2
  ADD
    CONSTRAINT target_2_pkey PRIMARY KEY (id);
END
$$;
