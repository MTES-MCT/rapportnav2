DO
$$
BEGIN
  CREATE TABLE IF NOT EXISTS public.mission_crew_absence (
     id                    SERIAL PRIMARY KEY,
     mission_crew_id       INTEGER NOT NULL REFERENCES public.mission_crew(id) ON DELETE CASCADE,

     start_date            DATE,
     end_date              DATE,
     is_absent_full_mission BOOLEAN,
     reason                TEXT,

     created_at            TIMESTAMPTZ DEFAULT now(),
     updated_at            TIMESTAMPTZ,
     created_by            INTEGER,
     updated_by            INTEGER
  );

  -- Index for fast filtering by mission crew
  CREATE INDEX IF NOT EXISTS idx_mission_crew_absence_crew_id
    ON public.mission_crew_absence (mission_crew_id);

END
$$;
