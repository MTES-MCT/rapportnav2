DO $$
DECLARE
    max_id INT;
BEGIN
    -- Check if the table mission_general_info already exists
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'mission_general_info') THEN

        -- Retrieve the maximum value of the id column
        SELECT MAX(id) INTO max_id FROM mission_general_info;

        -- Create a sequence that starts from 1 (default, in case the table is empty)
        CREATE SEQUENCE mission_general_info_id_seq
            START WITH 1
            INCREMENT BY 1
            OWNED BY mission_general_info.id;

        -- If the table is not empty, adjust the sequence to start from max_id + 1
        IF max_id IS NOT NULL THEN
            -- Update the sequence to start from max_id + 1
            EXECUTE 'ALTER SEQUENCE mission_general_info_id_seq RESTART WITH ' || (max_id + 1);
        END IF;

        -- Modify the id column to use the sequence as its default value
        ALTER TABLE mission_general_info
            ALTER COLUMN id SET DEFAULT nextval('mission_general_info_id_seq');

    END IF;
END $$;
