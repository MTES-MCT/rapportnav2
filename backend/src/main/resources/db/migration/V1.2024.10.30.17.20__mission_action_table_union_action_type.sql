DO
$$
  BEGIN

CREATE
  TYPE "ActionType"
  AS ENUM ('SURVEILLANCE',
          'CONTACT',
          'NOTE',
          'STATUS',
          'RESCUE',
          'OTHER',
          'NAUTICAL_EVENT',
          'ANTI_POLLUTION',
          'BAAEM_PERMANENCE',
          'VIGIMER',
          'PUBLIC_ORDER',
          'REPRESENTATION',
          'ILLEGAL_IMMIGRATION');

CREATE TABLE
  mission_action (
                          id uuid NOT NULL,
                          mission_id integer NOT NULL,
                          action_type "ActionType" NOT NULL,
                          start_datetime_utc timestamp with time zone NOT NULL,
                          end_datetime_utc timestamp with time zone NULL,
                          observations text NULL,
                          is_complete_for_stats boolean NULL,
                          latitude double precision NULL,
                          longitude double precision NULL,
                          detected_pollution boolean NULL,
                          pollution_observed_by_authorized_agent boolean NULL,
                          diversion_carried_out boolean NULL,
                          simple_brewing_operation boolean NULL,
                          anti_pol_device_deployed boolean NULL,
                          control_method character varying(16) NOT NULL,
                          vessel_identifier character varying(64) NULL,
                          vessel_type character varying(32) NULL,
                          vessel_size character varying(32) NULL,
                          identity_controlled_person character varying(128) NULL,
                          nb_of_intercepted_vessels integer NULL,
                          nb_of_intercepted_migrants integer NULL,
                          nb_of_suspected_smugglers integer NULL,
                          is_vessel_rescue boolean NULL,
                          is_person_rescue boolean NULL,
                          is_vessel_noticed boolean NULL,
                          is_vessel_towed boolean NULL,
                          is_in_srr_or_followed_by_cross_mrcc boolean NULL,
                          number_persons_rescued integer NULL,
                          number_of_deaths integer NULL,
                          operation_follows_defrep boolean NULL,
                          location_description text NULL,
                          is_migration_rescue boolean NULL,
                          nb_vessels_tracked_without_intervention integer NULL,
                          nb_assisted_vessels_returning_to_shore integer NULL,
                          status character varying(64) NULL,
                          reason character varying(64) NULL
);

ALTER TABLE
  mission_action
  ADD
    CONSTRAINT mission_action_pkey PRIMARY KEY (id);
  END
$$;
