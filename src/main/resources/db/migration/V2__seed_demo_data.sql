INSERT INTO consumer_units (code, name, owner_name, type, fixed_share, active, created_at) VALUES
('AP-101', 'Apartamento 101', 'Ana Souza', 'APARTMENT', 0.2500, TRUE, CURRENT_TIMESTAMP),
('AP-102', 'Apartamento 102', 'Bruno Lima', 'APARTMENT', 0.3000, TRUE, CURRENT_TIMESTAMP),
('AP-201', 'Apartamento 201', 'Carla Mendes', 'APARTMENT', 0.2000, TRUE, CURRENT_TIMESTAMP),
('AREA-COMUM', 'Area comum', 'Condominio Solar', 'COMMON_AREA', 0.2500, TRUE, CURRENT_TIMESTAMP);

INSERT INTO energy_readings (consumer_unit_id, type, energy_kwh, recorded_at, source)
SELECT id, 'CONSUMPTION', CASE code
    WHEN 'AP-101' THEN 320.0000 WHEN 'AP-102' THEN 410.0000
    WHEN 'AP-201' THEN 280.0000 ELSE 190.0000 END,
    CURRENT_TIMESTAMP - INTERVAL '5 days', 'IOT' FROM consumer_units;

INSERT INTO energy_readings (consumer_unit_id, type, energy_kwh, recorded_at, source)
SELECT id, 'GENERATION', CASE code
    WHEN 'AP-101' THEN 180.0000 WHEN 'AP-102' THEN 220.0000
    WHEN 'AP-201' THEN 150.0000 ELSE 480.0000 END,
    CURRENT_TIMESTAMP - INTERVAL '4 days', 'IOT' FROM consumer_units;

INSERT INTO alerts (consumer_unit_id, severity, status, title, message, created_at)
SELECT id, 'MEDIUM', 'OPEN', 'Comunicacao instavel', 'O medidor apresentou duas falhas de comunicacao nas ultimas 24 horas.', CURRENT_TIMESTAMP
FROM consumer_units WHERE code = 'AP-201';
