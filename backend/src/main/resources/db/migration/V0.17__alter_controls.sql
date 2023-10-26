DO $$
BEGIN

    -- 1. Drop the 'confirmed' column
    ALTER TABLE control_administrative
    DROP COLUMN confirmed;

    -- 2. Add the 'unit_should_confirm' boolean column
    ALTER TABLE control_administrative
    ADD COLUMN unit_should_confirm BOOLEAN;

    -- 3. Add the 'unit_has_confirmed' boolean column
    ALTER TABLE control_administrative
    ADD COLUMN unit_has_confirmed BOOLEAN;

    -- 4. Add the 'amount_of_controls' integer column
    ALTER TABLE control_administrative
    ADD COLUMN amount_of_controls INT DEFAULT 1 NOT NULL;

    -------------------------------------------------------------------
    -- 1. Drop the 'confirmed' column
    ALTER TABLE control_navigation
    DROP COLUMN confirmed;

    -- 2. Add the 'unit_should_confirm' boolean column
    ALTER TABLE control_navigation
    ADD COLUMN unit_should_confirm BOOLEAN;

    -- 3. Add the 'unit_has_confirmed' boolean column
    ALTER TABLE control_navigation
    ADD COLUMN unit_has_confirmed BOOLEAN;

    -- 4. Add the 'amount_of_controls' integer column
    ALTER TABLE control_navigation
    ADD COLUMN amount_of_controls INT DEFAULT 1 NOT NULL;
    -------------------------------------------------------------------
    -- 1. Drop the 'confirmed' column
    ALTER TABLE control_security
    DROP COLUMN confirmed;

    -- 2. Add the 'unit_should_confirm' boolean column
    ALTER TABLE control_security
    ADD COLUMN unit_should_confirm BOOLEAN;

    -- 3. Add the 'unit_has_confirmed' boolean column
    ALTER TABLE control_security
    ADD COLUMN unit_has_confirmed BOOLEAN;

    -- 4. Add the 'amount_of_controls' integer column
    ALTER TABLE control_security
    ADD COLUMN amount_of_controls INT DEFAULT 1 NOT NULL;
    -------------------------------------------------------------------
    -- 1. Drop the 'confirmed' column
    ALTER TABLE control_gens_de_mer
    DROP COLUMN confirmed;

    -- 2. Add the 'unit_should_confirm' boolean column
    ALTER TABLE control_gens_de_mer
    ADD COLUMN unit_should_confirm BOOLEAN;

    -- 3. Add the 'unit_has_confirmed' boolean column
    ALTER TABLE control_gens_de_mer
    ADD COLUMN unit_has_confirmed BOOLEAN;

    -- 4. Add the 'amount_of_controls' integer column
    ALTER TABLE control_gens_de_mer
    ADD COLUMN amount_of_controls INT DEFAULT 1 NOT NULL;

END $$;
