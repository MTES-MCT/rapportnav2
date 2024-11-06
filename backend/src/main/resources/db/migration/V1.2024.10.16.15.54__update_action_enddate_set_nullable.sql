DO
$$
  BEGIN

    ALTER TABLE public.mission_action_vigimer
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_rescue
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_representation
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_public_order
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_nautical_event
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_illegal_immigration
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_control
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_baaem_permanence
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

    ALTER TABLE public.mission_action_anti_pollution
      ALTER COLUMN end_datetime_utc DROP NOT NULL;

  END
$$;
