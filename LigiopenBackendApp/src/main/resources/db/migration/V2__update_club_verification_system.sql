-- Migration V2: Update Club entity with dual verification system
-- This script updates the clubs table to support both LigiOpen and FKF verification

-- Drop old verification columns if they exist
ALTER TABLE clubs DROP COLUMN IF EXISTS is_verified;
ALTER TABLE clubs DROP COLUMN IF EXISTS verification_status;

-- Add LigiOpen internal verification columns
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS is_ligiopen_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_status VARCHAR(50) DEFAULT 'PENDING';
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_date TIMESTAMP;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS ligiopen_verification_notes TEXT;

-- Add FKF official verification columns
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS is_fkf_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_verification_status VARCHAR(50) DEFAULT 'NOT_APPLICABLE';
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_verification_date TIMESTAMP;
ALTER TABLE clubs ADD COLUMN IF NOT EXISTS fkf_registration_date DATE;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_clubs_ligiopen_verification_status ON clubs(ligiopen_verification_status);
CREATE INDEX IF NOT EXISTS idx_clubs_fkf_verification_status ON clubs(fkf_verification_status);
CREATE INDEX IF NOT EXISTS idx_clubs_is_ligiopen_verified ON clubs(is_ligiopen_verified);
CREATE INDEX IF NOT EXISTS idx_clubs_is_fkf_verified ON clubs(is_fkf_verified);

-- Add check constraints for enum values
-- Drop constraints if they exist first
DO $$ 
BEGIN
    ALTER TABLE clubs DROP CONSTRAINT IF EXISTS chk_ligiopen_verification_status;
    ALTER TABLE clubs DROP CONSTRAINT IF EXISTS chk_fkf_verification_status;
EXCEPTION WHEN undefined_object THEN 
    NULL;
END $$;

-- Add the constraints
ALTER TABLE clubs ADD CONSTRAINT chk_ligiopen_verification_status 
    CHECK (ligiopen_verification_status IN ('PENDING', 'VERIFIED', 'REJECTED', 'SUSPENDED', 'UNDER_REVIEW'));

ALTER TABLE clubs ADD CONSTRAINT chk_fkf_verification_status 
    CHECK (fkf_verification_status IN ('NOT_APPLICABLE', 'PENDING', 'VERIFIED', 'EXPIRED', 'INVALID', 'SUSPENDED'));

-- Update existing records to set default values
UPDATE clubs SET 
    is_ligiopen_verified = COALESCE(is_ligiopen_verified, FALSE),
    ligiopen_verification_status = COALESCE(ligiopen_verification_status, 'PENDING'),
    is_fkf_verified = COALESCE(is_fkf_verified, FALSE),
    fkf_verification_status = CASE 
        WHEN registration_number IS NOT NULL AND registration_number != '' THEN 'PENDING'
        ELSE 'NOT_APPLICABLE'
    END
WHERE is_ligiopen_verified IS NULL OR ligiopen_verification_status IS NULL;