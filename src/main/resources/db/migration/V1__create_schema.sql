CREATE TABLE consumer_units (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    owner_name VARCHAR(120) NOT NULL,
    type VARCHAR(30) NOT NULL,
    fixed_share NUMERIC(7,4) NOT NULL CHECK (fixed_share > 0 AND fixed_share <= 1),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE energy_readings (
    id BIGSERIAL PRIMARY KEY,
    consumer_unit_id BIGINT NOT NULL REFERENCES consumer_units(id),
    type VARCHAR(20) NOT NULL,
    energy_kwh NUMERIC(14,4) NOT NULL CHECK (energy_kwh >= 0),
    recorded_at TIMESTAMPTZ NOT NULL,
    source VARCHAR(30) NOT NULL
);
CREATE INDEX idx_reading_unit_time ON energy_readings(consumer_unit_id, recorded_at);

CREATE TABLE credit_distributions (
    id BIGSERIAL PRIMARY KEY,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    total_credits_kwh NUMERIC(14,4) NOT NULL CHECK (total_credits_kwh > 0),
    strategy VARCHAR(40) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CHECK (period_start <= period_end)
);

CREATE TABLE credit_allocations (
    id BIGSERIAL PRIMARY KEY,
    distribution_id BIGINT NOT NULL REFERENCES credit_distributions(id) ON DELETE CASCADE,
    consumer_unit_id BIGINT NOT NULL REFERENCES consumer_units(id),
    credit_kwh NUMERIC(14,4) NOT NULL CHECK (credit_kwh >= 0),
    applied_share NUMERIC(7,4) NOT NULL CHECK (applied_share >= 0),
    UNIQUE(distribution_id, consumer_unit_id)
);

CREATE TABLE alerts (
    id BIGSERIAL PRIMARY KEY,
    consumer_unit_id BIGINT NOT NULL REFERENCES consumer_units(id),
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    title VARCHAR(80) NOT NULL,
    message VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    resolved_at TIMESTAMPTZ
);
CREATE INDEX idx_alert_status_severity ON alerts(status, severity);
