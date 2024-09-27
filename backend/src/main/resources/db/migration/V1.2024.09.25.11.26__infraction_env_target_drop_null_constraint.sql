DO
$$
BEGIN
  ALTER TABLE infraction_env_target
  ALTER vessel_size DROP NOT NULL,
  ALTER vessel_type DROP NOT NULL,
  ALTER vessel_identifier DROP NOT NULL;
END $$;
