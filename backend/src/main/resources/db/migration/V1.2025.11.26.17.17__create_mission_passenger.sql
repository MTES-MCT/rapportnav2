DO
$$
BEGIN

  CREATE TABLE IF NOT EXISTS mission_passenger (
   id SERIAL PRIMARY KEY,

   mission_id INTEGER,
   mission_id_uuid UUID,

   full_name TEXT NOT NULL,

   organization TEXT,
   is_intern BOOLEAN,

   start_date DATE NOT NULL,
   end_date DATE NOT NULL,

   created_at TIMESTAMPTZ,
   updated_at TIMESTAMPTZ,

   created_by INTEGER,
   updated_by INTEGER
  );

  CREATE INDEX IF NOT EXISTS idx_mission_passenger_mission_id
    ON mission_passenger (mission_id);

  CREATE INDEX IF NOT EXISTS idx_mission_passenger_mission_uuid
    ON mission_passenger (mission_id_uuid);

END
$$;

