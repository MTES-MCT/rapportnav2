CREATE TABLE IF NOT EXISTS authentication_audit (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    email VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_auth_audit_timestamp ON authentication_audit(timestamp);
CREATE INDEX IF NOT EXISTS idx_auth_audit_user_id ON authentication_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_auth_audit_email ON authentication_audit(email);
CREATE INDEX IF NOT EXISTS idx_auth_audit_event_type ON authentication_audit(event_type);