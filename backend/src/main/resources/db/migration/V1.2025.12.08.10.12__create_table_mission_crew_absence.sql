DO
$$
BEGIN
  CREATE TABLE public.mission_crew_absence (
     id                    SERIAL PRIMARY KEY,
     mission_crew_id       INTEGER NOT NULL REFERENCES public.mission_crew(id) ON DELETE CASCADE,

     start_datetime_utc    TIMESTAMPTZ,
     end_datetime_utc      TIMESTAMPTZ,
     is_absent_full_mission BOOLEAN,
     reason                TEXT,

     created_at            TIMESTAMPTZ DEFAULT now(),
     updated_at            TIMESTAMPTZ,
     created_by            INTEGER,
     updated_by            INTEGER
  );

  -- Index for fast filtering by mission crew
  CREATE INDEX idx_mission_crew_absence_crew_id
    ON public.mission_crew_absence (mission_crew_id);


  -- prevent overlapping absences for the same mission_crew
  -- Requires btree_gist extension (safe to include)
  CREATE EXTENSION IF NOT EXISTS btree_gist;

  ALTER TABLE public.mission_crew_absence
    ADD CONSTRAINT mission_crew_absence_no_overlap
      EXCLUDE USING gist (
      mission_crew_id WITH =,
      tstzrange(start_datetime_utc, end_datetime_utc) WITH &&
      );

END
$$;
