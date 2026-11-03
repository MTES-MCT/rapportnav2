CREATE TABLE IF NOT EXISTS export_audit
(
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id      VARCHAR(50) NOT NULL,
  action_id     VARCHAR(50) NOT NULL,
  document_type VARCHAR(50) NOT NULL,
  exported_by   INTEGER     NOT NULL,
  exported_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_export_audit_action_id ON export_audit (action_id);
