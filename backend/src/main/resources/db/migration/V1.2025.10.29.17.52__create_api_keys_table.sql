CREATE TABLE IF NOT EXISTS api_key (
    id UUID PRIMARY KEY,
    public_id VARCHAR(100) UNIQUE NOT NULL,
    hashed_key VARCHAR(255) NOT NULL,
    owner VARCHAR(100),

    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    created_by INT NULL,
    updated_by INT NULL,

    disabled_at TIMESTAMP
);

