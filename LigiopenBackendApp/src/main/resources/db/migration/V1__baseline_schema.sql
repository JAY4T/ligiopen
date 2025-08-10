-- Migration V1: Baseline Schema
-- This migration establishes the baseline for existing schema
-- Since we're switching from Hibernate DDL to Flyway management

-- This file intentionally left minimal since baseline-on-migrate=true
-- will handle existing schema automatically

-- Create flyway_schema_history table if it doesn't exist (handled automatically)
-- Existing tables will be baselined automatically

-- Add a simple comment to mark this migration as completed
SELECT 'Baseline migration completed - existing schema baselined' as migration_status;