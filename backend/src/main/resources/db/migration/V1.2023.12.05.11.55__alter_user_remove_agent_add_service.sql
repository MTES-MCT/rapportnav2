DO $$
BEGIN
    -- Drop the foreign key constraint
    ALTER TABLE "user" DROP CONSTRAINT IF EXISTS fk_user_agent_id;

    -- Drop the existing column
    ALTER TABLE "user" DROP COLUMN IF EXISTS agent_id;

    -- Add the new column with a foreign key constraint allowing NULL
    ALTER TABLE "user" ADD COLUMN service_id INTEGER REFERENCES service(id);

    -- Add a foreign key constraint explicitly allowing NULL
    ALTER TABLE "user" ADD CONSTRAINT fk_user_service_id
        FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE SET NULL;

END $$;