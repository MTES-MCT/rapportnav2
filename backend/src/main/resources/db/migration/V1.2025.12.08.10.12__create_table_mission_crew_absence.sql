DO
$$
BEGIN
  CREATE TABLE IF NOT EXISTS public.mission_crew_absence (
     id                    SERIAL PRIMARY KEY,
     mission_crew_id       INTEGER NOT NULL REFERENCES public.mission_crew(id) ON DELETE CASCADE,

     start_date            DATE NOT NULL,
     end_date              DATE NOT NULL,
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


  -- prevent overlapping absences for the same mission_crew
  -- Requires btree_gist extension (safe to include)
  CREATE EXTENSION IF NOT EXISTS btree_gist;

  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conname = 'mission_crew_absence_no_overlap'
    ) THEN
  ALTER TABLE public.mission_crew_absence
    ADD CONSTRAINT mission_crew_absence_no_overlap
      EXCLUDE USING gist (
      mission_crew_id WITH =,
      daterange(start_date, end_date, '[]') WITH &&
      );
END IF;
END
$$;
