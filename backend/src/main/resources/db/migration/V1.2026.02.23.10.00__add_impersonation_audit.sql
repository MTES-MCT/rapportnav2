CREATE TABLE IF NOT EXISTS impersonation_audit (
    id SERIAL PRIMARY KEY,
    admin_user_id INTEGER NOT NULL,
    target_service_id INTEGER NOT NULL,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_impersonation_audit_admin_user_id ON impersonation_audit(admin_user_id);
CREATE INDEX IF NOT EXISTS idx_impersonation_audit_timestamp ON impersonation_audit(timestamp);
