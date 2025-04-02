DO
$$
BEGIN
ALTER TABLE infraction_2
ALTER COLUMN control_id DROP NOT NULL;

ALTER TABLE control_2
ALTER COLUMN target_id DROP NOT NULL;

ALTER TABLE target_2
ALTER COLUMN identity_controlled_person DROP NOT NULL;

END
$$;
