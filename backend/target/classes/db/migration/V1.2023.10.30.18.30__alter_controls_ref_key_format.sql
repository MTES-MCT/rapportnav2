DO $$
BEGIN
    -- Change the data type of action_control_id from UUID to VARCHAR(36)

    -- First, create a new temporary column with the desired data type
    ALTER TABLE control_security
    ADD COLUMN action_control_id_temp VARCHAR(36);

    -- Update the temporary column with data from the original UUID column
    UPDATE control_security
    SET action_control_id_temp = action_control_id::VARCHAR;

    -- Drop the original UUID column
    ALTER TABLE control_security
    DROP COLUMN action_control_id;

    -- Rename the temporary column to action_control_id
    ALTER TABLE control_security
    RENAME COLUMN action_control_id_temp TO action_control_id;
END $$;

DO $$
BEGIN
    -- Change the data type of action_control_id from UUID to VARCHAR(36)

    -- First, create a new temporary column with the desired data type
    ALTER TABLE control_administrative
    ADD COLUMN action_control_id_temp VARCHAR(36);

    -- Update the temporary column with data from the original UUID column
    UPDATE control_administrative
    SET action_control_id_temp = action_control_id::VARCHAR;

    -- Drop the original UUID column
    ALTER TABLE control_administrative
    DROP COLUMN action_control_id;

    -- Rename the temporary column to action_control_id
    ALTER TABLE control_administrative
    RENAME COLUMN action_control_id_temp TO action_control_id;
END $$;

DO $$
BEGIN
    -- Change the data type of action_control_id from UUID to VARCHAR(36)

    -- First, create a new temporary column with the desired data type
    ALTER TABLE control_navigation
    ADD COLUMN action_control_id_temp VARCHAR(36);

    -- Update the temporary column with data from the original UUID column
    UPDATE control_navigation
    SET action_control_id_temp = action_control_id::VARCHAR;

    -- Drop the original UUID column
    ALTER TABLE control_navigation
    DROP COLUMN action_control_id;

    -- Rename the temporary column to action_control_id
    ALTER TABLE control_navigation
    RENAME COLUMN action_control_id_temp TO action_control_id;
END $$;

DO $$
BEGIN
    -- Change the data type of action_control_id from UUID to VARCHAR(36)

    -- First, create a new temporary column with the desired data type
    ALTER TABLE control_gens_de_mer
    ADD COLUMN action_control_id_temp VARCHAR(36);

    -- Update the temporary column with data from the original UUID column
    UPDATE control_gens_de_mer
    SET action_control_id_temp = action_control_id::VARCHAR;

    -- Drop the original UUID column
    ALTER TABLE control_gens_de_mer
    DROP COLUMN action_control_id;

    -- Rename the temporary column to action_control_id
    ALTER TABLE control_gens_de_mer
    RENAME COLUMN action_control_id_temp TO action_control_id;
END $$;
