DO $$
BEGIN
    ALTER TABLE mission_action_free_note
    DROP COLUMN end_datetime_utc;

    ALTER TABLE mission_action_free_note
    ALTER COLUMN observations DROP NOT NULL;
END $$;
