DO $$
BEGIN
    -- Create a new column with a foreign key constraint allowing NULL
    ALTER TABLE "user"
    ADD COLUMN agent_id INTEGER REFERENCES "agent"(id);

    -- Add a foreign key constraint explicitly allowing NULL
    ALTER TABLE "user"
    ADD CONSTRAINT fk_user_agent_id
    FOREIGN KEY (agent_id) REFERENCES "agent"(id) ON DELETE SET NULL;
END $$;
