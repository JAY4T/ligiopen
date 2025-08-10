-- Manual Database Update Script
-- Update Club entity with dual verification system
-- Run this script directly on your PostgreSQL database

-- ========================================
-- STEP 1: Backup existing data (recommended)
-- ========================================
-- CREATE TABLE clubs_backup AS SELECT * FROM clubs;

-- ========================================
-- STEP 2: Drop old verification columns 
-- ========================================
ALTER TABLE clubs DROP COLUMN IF EXISTS is_verified;
ALTER TABLE clubs DROP COLUMN IF EXISTS verification_status;

-- ========================================
-- STEP 3: Add LigiOpen verification columns
-- ========================================
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS is_ligiopen_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_status VARCHAR(50) DEFAULT 'PENDING';
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_date TIMESTAMP;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_notes TEXT;

-- ========================================
-- STEP 4: Add FKF verification columns
-- ========================================
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS is_fkf_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_verification_status VARCHAR(50) DEFAULT 'NOT_APPLICABLE';
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_verification_date TIMESTAMP;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_registration_date DATE;

-- ========================================
-- STEP 5: Add constraints for data integrity
-- ========================================
ALTER TABLE clubs ADD CONSTRAINT IF NOT EXISTS chk_ligiopen_verification_status 
    CHECK (ligiopen_verification_status IN ('PENDING', 'VERIFIED', 'REJECTED', 'SUSPENDED', 'UNDER_REVIEW'));

ALTER TABLE clubs ADD CONSTRAINT IF NOT EXISTS chk_fkf_verification_status 
    CHECK (fkf_verification_status IN ('NOT_APPLICABLE', 'PENDING', 'VERIFIED', 'EXPIRED', 'INVALID', 'SUSPENDED'));

-- ========================================
-- STEP 6: Create indexes for performance
-- ========================================
CREATE INDEX IF NOT EXISTS idx_clubs_ligiopen_verification_status ON clubs(ligiopen_verification_status);
CREATE INDEX IF NOT EXISTS idx_clubs_fkf_verification_status ON clubs(fkf_verification_status);
CREATE INDEX IF NOT EXISTS idx_clubs_is_ligiopen_verified ON clubs(is_ligiopen_verified);
CREATE INDEX IF NOT EXISTS idx_clubs_is_fkf_verified ON clubs(is_fkf_verified);
CREATE INDEX IF NOT EXISTS idx_clubs_registration_number ON clubs(registration_number) WHERE registration_number IS NOT NULL;

-- ========================================
-- STEP 7: Update existing records with smart defaults
-- ========================================
UPDATE clubs SET 
    is_ligiopen_verified = FALSE,
    ligiopen_verification_status = 'PENDING',
    is_fkf_verified = FALSE,
    fkf_verification_status = CASE 
        WHEN registration_number IS NOT NULL AND registration_number != '' THEN 'PENDING'
        ELSE 'NOT_APPLICABLE'
    END
WHERE is_ligiopen_verified IS NULL;

-- ========================================
-- STEP 8: Verify the changes (run these queries)
-- ========================================
-- SELECT 
--     name, 
--     registration_number,
--     is_ligiopen_verified,
--     ligiopen_verification_status,
--     is_fkf_verified,
--     fkf_verification_status,
--     club_level
-- FROM clubs 
-- LIMIT 10;

-- SELECT 
--     ligiopen_verification_status, 
--     COUNT(*) 
-- FROM clubs 
-- GROUP BY ligiopen_verification_status;

-- SELECT 
--     fkf_verification_status, 
--     COUNT(*) 
-- FROM clubs 
-- GROUP BY fkf_verification_status;

COMMIT;