DO
$$
  BEGIN

    ALTER TABLE inquiry
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE mission
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE mission_general_info
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE mission_crew
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE mission_action
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE target_2
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE control_2
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE infraction_2
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE inter_ministerial_service
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE agent
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE agent_role
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE agent_service
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE service
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

    ALTER TABLE public.user
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


  END
$$;
