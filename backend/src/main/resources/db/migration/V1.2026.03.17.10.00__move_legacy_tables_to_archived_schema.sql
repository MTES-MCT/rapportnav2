-- Migration: Move legacy tables to archived schema
-- These tables have been replaced by the unified mission_action table and the new v2 schema (target_2, control_2, infraction_2)

-- 1️⃣ Create the archived schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS archived;

-- 2️⃣ Move tables to archived schema
-- Note: ALTER TABLE ... SET SCHEMA will also move indexes, constraints, and triggers associated with the table
-- Since agent and agent_service are both moving to archived, FK constraints between them remain valid

-- Agent table (must be moved before agent_service due to FK dependency)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'agent') THEN
        ALTER TABLE public.agent SET SCHEMA archived;
    END IF;
END $$;

-- Agent service table
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'agent_service') THEN
        ALTER TABLE public.agent_service SET SCHEMA archived;
    END IF;
END $$;

-- Control tables (FKs to mission_action_control were already dropped in V0.16)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'control_administrative') THEN
        ALTER TABLE public.control_administrative SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'control_gens_de_mer') THEN
        ALTER TABLE public.control_gens_de_mer SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'control_navigation') THEN
        ALTER TABLE public.control_navigation SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'control_security') THEN
        ALTER TABLE public.control_security SET SCHEMA archived;
    END IF;
END $$;

-- Infraction tables (FK from infraction_natinf to infraction was already dropped in V1.2023.12.14)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'infraction') THEN
        ALTER TABLE public.infraction SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'infraction_natinf') THEN
        ALTER TABLE public.infraction_natinf SET SCHEMA archived;
    END IF;
END $$;

-- Mission action tables (all legacy action tables migrated to unified mission_action table in V1.2025.10.15.09.52)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_anti_pollution') THEN
        ALTER TABLE public.mission_action_anti_pollution SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_baaem_permanence') THEN
        ALTER TABLE public.mission_action_baaem_permanence SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_control') THEN
        ALTER TABLE public.mission_action_control SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_free_note') THEN
        ALTER TABLE public.mission_action_free_note SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_illegal_immigration') THEN
        ALTER TABLE public.mission_action_illegal_immigration SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_nautical_event') THEN
        ALTER TABLE public.mission_action_nautical_event SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_public_order') THEN
        ALTER TABLE public.mission_action_public_order SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_representation') THEN
        ALTER TABLE public.mission_action_representation SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_rescue') THEN
        ALTER TABLE public.mission_action_rescue SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_status') THEN
        ALTER TABLE public.mission_action_status SET SCHEMA archived;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'mission_action_vigimer') THEN
        ALTER TABLE public.mission_action_vigimer SET SCHEMA archived;
    END IF;
END $$;