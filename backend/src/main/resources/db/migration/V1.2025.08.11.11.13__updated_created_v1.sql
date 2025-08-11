DO
$$
  BEGIN

    ALTER TABLE mission_action_anti_pollution
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_baaem_permanence
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_control
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_free_note
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_illegal_immigration
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_nautical_event
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_public_order
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_representation
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_rescue
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_status
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE mission_action_vigimer
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE control_administrative
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE control_gens_de_mer
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE control_navigation
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE control_security
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE infraction_env_target
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE infraction
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;


    ALTER TABLE infraction_natinf
      ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
      ADD COLUMN updated_at TIMESTAMP,
      ADD COLUMN created_by INTEGER,
      ADD COLUMN updated_by INTEGER;

  END
$$;
