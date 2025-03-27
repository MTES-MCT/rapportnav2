CREATE TYPE mission_type_enum AS ENUM ('AIR', 'LAND', 'SEA');

CREATE TABLE mission (
    id SERIAL PRIMARY KEY,
    mission_types mission_type_enum[] NOT NULL,
    control_units INTEGER[] NULL,
    open_by VARCHAR(255) NULL,
    completed_by VARCHAR(255) NULL,
    start_datetime_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    end_datetime_utc TIMESTAMP WITH TIME ZONE NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    mission_source VARCHAR(255) NULL,
    observations_by_unit TEXT NULL,
    control_unit_id_owner INTEGER NOT NULL
);
