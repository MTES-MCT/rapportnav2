CREATE TABLE IF NOT EXISTS api_key
(
  id           UUID PRIMARY KEY,
  public_id    VARCHAR(12) UNIQUE NOT NULL,
  hashed_key   VARCHAR(255)       NOT NULL,
  owner        VARCHAR(100),

  created_at   TIMESTAMP          NULL,
  updated_at   TIMESTAMP          NULL,
  created_by   INT                NULL,
  updated_by   INT                NULL,

  last_used_at TIMESTAMP          NULL,
  disabled_at  TIMESTAMP
);
-- Add index for faster lookup
CREATE INDEX IF NOT EXISTS idx_api_key_public_id ON api_key (public_id);
CREATE INDEX IF NOT EXISTS idx_api_key_disabled_at ON api_key (disabled_at);


CREATE TABLE IF NOT EXISTS api_key_audit
(
  id             SERIAL PRIMARY KEY,
  api_key_id     UUID,
  ip_address     VARCHAR(45),
  request_path   VARCHAR(500),
  success        BOOLEAN     NOT NULL,
  failure_reason VARCHAR(255),
  timestamp      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_audit_timestamp ON api_key_audit (timestamp);
CREATE INDEX IF NOT EXISTS idx_audit_api_key_id ON api_key_audit (api_key_id);
