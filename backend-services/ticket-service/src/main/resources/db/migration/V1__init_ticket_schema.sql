create extension if not exists pg_trgm;

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW', --NEW, OPEN, PENDING,RESOLVED, CLOSED
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM', --LOW, MEDIUM, HIGH, URGENT
    category VARCHAR(100),
    sentiment VARCHAR(20), 

    asignee_id BIGINT,
    requester_id BIGINT NOT NULL,
    requester_email VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,

    sla_breach_at TIMESTAMP,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP,

    ai_processed BOOLEAN DEFAULT FALSE,
    ai_response TEXT,
    ai_confidence DECIMAL(3,2),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT DEFAULT 0
);

CREATE TABLE ticket_comments(
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    user_name VARCHAR(255),
    body TEXT NOT NULL,
    is_internal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);

CREATE TABLE ticket_attachments (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_url TEXT NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255)
);

CREATE TABLE ticket_history (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    field_name VARCHAR(100) NOT NULL, 
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tickets_updated_at 
BEFORE UPDATE ON tickets
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE INDEX idx_comments_ticket_id ON ticket_comments(ticket_id);
CREATE INDEX idx_attachments_ticket_id ON ticket_attachments(ticket_id);
CREATE INDEX idx_history_ticket_id ON ticket_history(ticket_id);
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_tenant_id ON tickets(tenant_id);
CREATE INDEX idx_tickets_assignee_id ON tickets(assignee_id);
CREATE INDEX idx_tickets_requester_id ON tickets(requester_id);