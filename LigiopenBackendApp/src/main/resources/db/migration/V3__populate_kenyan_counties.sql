-- Migration V3: Populate Kenyan counties data
-- Insert all 47 Kenyan counties with their regions and codes
-- Data source: Kenya National Bureau of Statistics

-- Clear existing data if any (for clean migration)
DELETE FROM counties;

-- Insert all Kenyan counties in one statement (47 counties total)
INSERT INTO counties (name, code, region) VALUES
-- Central Region Counties
('Nairobi', '047', 'Central'),
('Kiambu', '022', 'Central'),
('Murang''a', '021', 'Central'),
('Nyeri', '018', 'Central'),
('Kirinyaga', '020', 'Central'),
('Nyandarua', '019', 'Central'),

-- Coast Region Counties
('Mombasa', '001', 'Coast'),
('Kilifi', '003', 'Coast'),
('Tana River', '006', 'Coast'),
('Lamu', '004', 'Coast'),
('Taita-Taveta', '005', 'Coast'),
('Kwale', '002', 'Coast'),

-- Eastern Region Counties
('Machakos', '016', 'Eastern'),
('Kitui', '015', 'Eastern'),
('Makueni', '017', 'Eastern'),
('Embu', '014', 'Eastern'),
('Tharaka-Nithi', '013', 'Eastern'),
('Meru', '012', 'Eastern'),
('Isiolo', '011', 'Eastern'),
('Marsabit', '010', 'Eastern'),

-- North Eastern Region Counties
('Garissa', '007', 'North Eastern'),
('Wajir', '008', 'North Eastern'),
('Mandera', '009', 'North Eastern'),

-- Nyanza Region Counties
('Siaya', '041', 'Nyanza'),
('Kisumu', '042', 'Nyanza'),
('Homa Bay', '043', 'Nyanza'),
('Migori', '044', 'Nyanza'),
('Kisii', '045', 'Nyanza'),
('Nyamira', '046', 'Nyanza'),

-- Rift Valley Region Counties
('Turkana', '023', 'Rift Valley'),
('West Pokot', '024', 'Rift Valley'),
('Samburu', '025', 'Rift Valley'),
('Trans Nzoia', '026', 'Rift Valley'),
('Uasin Gishu', '027', 'Rift Valley'),
('Elgeyo-Marakwet', '028', 'Rift Valley'),
('Nandi', '029', 'Rift Valley'),
('Baringo', '030', 'Rift Valley'),
('Laikipia', '031', 'Rift Valley'),
('Nakuru', '032', 'Rift Valley'),
('Narok', '033', 'Rift Valley'),
('Kajiado', '034', 'Rift Valley'),
('Kericho', '035', 'Rift Valley'),
('Bomet', '036', 'Rift Valley'),

-- Western Region Counties
('Kakamega', '037', 'Western'),
('Vihiga', '038', 'Western'),
('Bungoma', '039', 'Western'),
('Busia', '040', 'Western');

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_counties_region ON counties(region);
CREATE INDEX IF NOT EXISTS idx_counties_code ON counties(code);
CREATE INDEX IF NOT EXISTS idx_counties_name ON counties(name);

-- Add some major stadiums for key counties
INSERT INTO stadiums (name, city, town, county_id, capacity, surface_type, has_floodlights, is_verified, is_active, created_at) VALUES
-- Nairobi stadiums
('Kasarani Stadium', 'Nairobi', 'Kasarani', (SELECT id FROM counties WHERE name = 'Nairobi'), 60000, 'NATURAL_GRASS', true, true, true, NOW()),
('Nyayo National Stadium', 'Nairobi', 'Nairobi CBD', (SELECT id FROM counties WHERE name = 'Nairobi'), 30000, 'NATURAL_GRASS', true, true, true, NOW()),

-- Mombasa stadiums  
('Mombasa Municipal Stadium', 'Mombasa', 'Mombasa', (SELECT id FROM counties WHERE name = 'Mombasa'), 10000, 'NATURAL_GRASS', true, true, true, NOW()),

-- Kisumu stadiums
('Moi Stadium Kisumu', 'Kisumu', 'Kisumu', (SELECT id FROM counties WHERE name = 'Kisumu'), 35000, 'NATURAL_GRASS', true, true, true, NOW()),

-- Nakuru stadiums
('Afraha Stadium', 'Nakuru', 'Nakuru', (SELECT id FROM counties WHERE name = 'Nakuru'), 8200, 'NATURAL_GRASS', false, true, true, NOW());