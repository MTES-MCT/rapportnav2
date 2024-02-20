CREATE TABLE public.dummy (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    is_archived BOOLEAN NOT NULL
);
