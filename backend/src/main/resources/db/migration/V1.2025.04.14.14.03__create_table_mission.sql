CREATE TABLE mission (
    id uuid NOT NULL,
    service_id INTEGER NOT NULL,
    open_by VARCHAR(255) NULL,
    completed_by VARCHAR(255) NULL,
    start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    end_datetime_utc TIMESTAMP WITH TIME ZONE NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    mission_source VARCHAR(255) NULL,
    observations_by_unit TEXT NULL
);
