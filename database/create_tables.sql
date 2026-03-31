-- ============================================================
-- Professional Skills Testing System
-- Database Schema Creation Script
-- ============================================================

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS test_results CASCADE;
DROP TABLE IF EXISTS test_assignments CASCADE;
DROP TABLE IF EXISTS answer_options CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS tests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Table: users
-- Description: Stores user accounts (admins and specialists)
-- ============================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'specialist')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS 'Stores user accounts for admins and specialists';
COMMENT ON COLUMN users.id IS 'Primary key, auto-increment';
COMMENT ON COLUMN users.login IS 'Unique login name';
COMMENT ON COLUMN users.password_hash IS 'Hashed password (BCrypt)';
COMMENT ON COLUMN users.role IS 'User role: admin or specialist';
COMMENT ON COLUMN users.is_active IS 'Account active status';

-- ============================================================
-- Table: tests
-- Description: Stores test definitions
-- ============================================================
CREATE TABLE tests (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    time_limit INTEGER CHECK (time_limit > 0),
    passing_score INTEGER CHECK (passing_score >= 0 AND passing_score <= 100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE tests IS 'Stores test definitions and configurations';
COMMENT ON COLUMN tests.time_limit IS 'Time limit in minutes';
COMMENT ON COLUMN tests.passing_score IS 'Minimum passing score percentage';

-- ============================================================
-- Table: questions
-- Description: Stores questions for tests (identifying relationship with tests)
-- ============================================================
CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    test_id INTEGER NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    question_type VARCHAR(20) NOT NULL CHECK (question_type IN ('single', 'multiple')),
    order_num INTEGER NOT NULL,
    UNIQUE (test_id, order_num)
);

COMMENT ON TABLE questions IS 'Stores questions belonging to tests';
COMMENT ON COLUMN questions.test_id IS 'Foreign key to tests table (identifying relationship)';
COMMENT ON COLUMN questions.question_type IS 'Type: single (one correct) or multiple (multiple correct)';
COMMENT ON COLUMN questions.order_num IS 'Display order of question within test';

-- ============================================================
-- Table: answer_options
-- Description: Stores answer options for questions (identifying relationship with questions)
-- ============================================================
CREATE TABLE answer_options (
    id SERIAL PRIMARY KEY,
    question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    order_num INTEGER NOT NULL,
    UNIQUE (question_id, order_num)
);

COMMENT ON TABLE answer_options IS 'Stores answer options for questions';
COMMENT ON COLUMN answer_options.question_id IS 'Foreign key to questions table (identifying relationship)';
COMMENT ON COLUMN answer_options.is_correct IS 'Indicates if this is a correct answer';
COMMENT ON COLUMN answer_options.order_num IS 'Display order of option within question';

-- ============================================================
-- Table: test_assignments
-- Description: Stores test assignments to users
-- ============================================================
CREATE TABLE test_assignments (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    test_id INTEGER NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
    assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline TIMESTAMP,
    attempts_left INTEGER DEFAULT 1,
    status VARCHAR(20) DEFAULT 'assigned' CHECK (status IN ('assigned', 'in_progress', 'completed', 'expired')),
    UNIQUE (user_id, test_id)
);

COMMENT ON TABLE test_assignments IS 'Stores test assignments to specialists';
COMMENT ON COLUMN test_assignments.user_id IS 'Foreign key to users (specialist)';
COMMENT ON COLUMN test_assignments.test_id IS 'Foreign key to tests';
COMMENT ON COLUMN test_assignments.attempts_left IS 'Remaining attempts';
COMMENT ON COLUMN test_assignments.status IS 'Assignment status';

-- ============================================================
-- Table: test_results
-- Description: Stores test completion results
-- ============================================================
CREATE TABLE test_results (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    test_id INTEGER NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    score_percent DECIMAL(5,2),
    is_passed BOOLEAN,
    attempts_used INTEGER DEFAULT 1,
    answers_data TEXT
);

COMMENT ON TABLE test_results IS 'Stores test completion results';
COMMENT ON COLUMN test_results.user_id IS 'Foreign key to users (specialist)';
COMMENT ON COLUMN test_results.test_id IS 'Foreign key to tests';
COMMENT ON COLUMN test_results.score_percent IS 'Score percentage achieved';
COMMENT ON COLUMN test_results.is_passed IS 'Whether test was passed';
COMMENT ON COLUMN test_results.answers_data IS 'JSON data of user answers';

-- ============================================================
-- Indexes for performance
-- ============================================================
CREATE INDEX idx_users_login ON users(login);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_tests_active ON tests(is_active);
CREATE INDEX idx_questions_test_id ON questions(test_id);
CREATE INDEX idx_answer_options_question_id ON answer_options(question_id);
CREATE INDEX idx_assignments_user_id ON test_assignments(user_id);
CREATE INDEX idx_assignments_test_id ON test_assignments(test_id);
CREATE INDEX idx_assignments_status ON test_assignments(status);
CREATE INDEX idx_results_user_id ON test_results(user_id);
CREATE INDEX idx_results_test_id ON test_results(test_id);
