CREATE TABLE IF NOT EXISTS public.mission_resource_usage (
    id                SERIAL PRIMARY KEY,
    mission_id_uuid   UUID NOT NULL,
    resource_id       INTEGER NOT NULL,
    nb_kms            DOUBLE PRECISION,
    nb_engine_hours   DOUBLE PRECISION,
    created_at        TIMESTAMP DEFAULT NOW(),
    updated_at        TIMESTAMP,
    created_by        INTEGER,
    updated_by        INTEGER,
    CONSTRAINT fk_mission_resource_usage_mission
        FOREIGN KEY (mission_id_uuid)
        REFERENCES mission(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_mission_resource_usage UNIQUE (mission_id_uuid, resource_id)
);

CREATE INDEX IF NOT EXISTS idx_mission_resource_usage_mission_id_uuid
    ON public.mission_resource_usage (mission_id_uuid);
