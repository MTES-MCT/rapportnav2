---------------------------------------------------------------------
-- 1️⃣ Create enum type "ServiceType" if it does not exist
---------------------------------------------------------------------
DO $$
  BEGIN
    IF NOT EXISTS (
      SELECT 1 FROM pg_type WHERE typname = 'ServiceType'
    ) THEN
      CREATE TYPE "ServiceType" AS ENUM ('PAM', 'ULAM');
      ALTER TYPE "ServiceType" OWNER TO postgres;
    END IF;
  END$$;

---------------------------------------------------------------------
-- 2️⃣ Add column service_type if missing
---------------------------------------------------------------------
ALTER TABLE public.service
  ADD COLUMN IF NOT EXISTS service_type "ServiceType";

---------------------------------------------------------------------
-- 3️⃣ Set default = ULAM for all rows
---------------------------------------------------------------------
UPDATE public.service
SET service_type = 'ULAM'
WHERE service_type IS NULL;

---------------------------------------------------------------------
-- 4️⃣ Set first 8 rows (by ID) to PAM
---------------------------------------------------------------------
UPDATE public.service
SET service_type = 'PAM'
WHERE id IN (SELECT id FROM public.service ORDER BY id ASC LIMIT 8);

---------------------------------------------------------------------
-- 5️⃣ Add FK mission_general_info.service_id → service.id if missing
---------------------------------------------------------------------
DO $$
  BEGIN
    IF NOT EXISTS (
      SELECT 1
      FROM pg_constraint
      WHERE conname = 'fk_mission_general_info_service'
    ) THEN
      ALTER TABLE public.mission_general_info
        ADD CONSTRAINT fk_mission_info_service
          FOREIGN KEY (service_id)
            REFERENCES public.service(id)
            ON DELETE SET NULL;
    END IF;
  END$$;
