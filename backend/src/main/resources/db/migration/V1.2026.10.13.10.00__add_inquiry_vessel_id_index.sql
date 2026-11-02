CREATE INDEX IF NOT EXISTS idx_inquiry_vessel_id_start_datetime_utc
    ON inquiry (vessel_id, start_datetime_utc DESC);
