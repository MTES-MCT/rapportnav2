DO $$
BEGIN
    ALTER TABLE "agent_service"
    ADD COLUMN id SERIAL NOT NULL,
    ADD COLUMN "agent_role_id" INTEGER,
    DROP CONSTRAINT "agent_service_pkey",
    ADD CONSTRAINT "agent_service_pkey" PRIMARY KEY (id),
    ADD CONSTRAINT "agent_service_role_id_fkey" FOREIGN KEY (agent_role_id) REFERENCES agent_role(id);
END $$;
