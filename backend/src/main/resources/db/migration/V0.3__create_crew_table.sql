CREATE TABLE public.crew (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    is_archived BOOLEAN NOT NULL
);
