-- ============================================================
-- Professional Skills Testing System
-- Database Drop Script
-- ============================================================
-- This script drops all tables in reverse order of dependencies
-- Use with caution - this will permanently delete all data!

-- Drop tables with CASCADE to handle foreign key dependencies
DROP TABLE IF EXISTS test_results CASCADE;
DROP TABLE IF EXISTS test_assignments CASCADE;
DROP TABLE IF EXISTS answer_options CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS tests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tests_id_seq CASCADE;
DROP SEQUENCE IF EXISTS questions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS answer_options_id_seq CASCADE;
DROP SEQUENCE IF EXISTS test_assignments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS test_results_id_seq CASCADE;

-- Confirmation message
SELECT 'All tables and sequences dropped successfully!' AS status;
