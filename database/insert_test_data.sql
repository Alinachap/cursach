INSERT INTO users (login, password_hash, first_name, last_name, role, is_active) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Administrator', 'admin', TRUE),
('admin2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maria', 'Ivanova', 'admin', TRUE);

INSERT INTO users (login, password_hash, first_name, last_name, role, is_active) VALUES
('specialist1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alexey', 'Petrov', 'specialist', TRUE),
('specialist2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Elena', 'Sidorova', 'specialist', TRUE),
('specialist3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dmitry', 'Kozlov', 'specialist', TRUE),
('specialist4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Olga', 'Smirnova', 'specialist', TRUE),
('blocked_user', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Blocked', 'User', 'specialist', FALSE);

INSERT INTO tests (title, description, time_limit, passing_score, is_active) VALUES
('Java Programming Basics', 'Fundamental concepts of Java programming language', 30, 70, TRUE),
('Database Design Principles', 'Relational database design and normalization', 45, 75, TRUE),
('Web Development Fundamentals', 'HTML, CSS, JavaScript basics for web development', 40, 65, TRUE),
('Software Testing Methods', 'Unit testing, integration testing, and QA practices', 35, 80, TRUE),
('Legacy Test (Inactive)', 'This is an old inactive test', 20, 50, FALSE);

INSERT INTO questions (test_id, question_text, question_type, order_num) VALUES
(1, 'Which keyword is used to define a class in Java?', 'single', 1),
(1, 'What is the correct way to declare a main method?', 'single', 2),
(1, 'Which of the following are primitive data types in Java? (Select all that apply)', 'multiple', 3),
(1, 'What does OOP stand for?', 'single', 4),
(1, 'Which access modifier provides the widest visibility?', 'single', 5),
(1, 'What is the purpose of the "final" keyword? (Select all that apply)', 'multiple', 6);

INSERT INTO answer_options (question_id, option_text, is_correct, order_num) VALUES
(1, 'class', TRUE, 1),
(1, 'def', FALSE, 2),
(1, 'struct', FALSE, 3),
(1, 'object', FALSE, 4),
(2, 'public static void main(String[] args)', TRUE, 1),
(2, 'public void main(String[] args)', FALSE, 2),
(2, 'static void main(String[] args)', FALSE, 3),
(2, 'public main(String[] args)', FALSE, 4),
(3, 'int', TRUE, 1),
(3, 'String', FALSE, 2),
(3, 'boolean', TRUE, 3),
(3, 'Integer', FALSE, 4),
(3, 'double', TRUE, 5),
(4, 'Object-Oriented Programming', TRUE, 1),
(4, 'Object Original Programming', FALSE, 2),
(4, 'Operational Object Process', FALSE, 3),
(4, 'Oriented Object Protocol', FALSE, 4),
(5, 'public', TRUE, 1),
(5, 'private', FALSE, 2),
(5, 'protected', FALSE, 3),
(5, 'default', FALSE, 4),
(6, 'Prevent method overriding', TRUE, 1),
(6, 'Make class immutable', FALSE, 2),
(6, 'Prevent inheritance', TRUE, 3),
(6, 'Make variable constant', TRUE, 4);

INSERT INTO questions (test_id, question_text, question_type, order_num) VALUES
(2, 'What is the primary key?', 'single', 1),
(2, 'Which normal form eliminates transitive dependencies?', 'single', 2),
(2, 'What does ACID stand for? (Select all that apply)', 'multiple', 3),
(2, 'What is a foreign key?', 'single', 4),
(2, 'Which SQL statement is used to retrieve data?', 'single', 5);

INSERT INTO answer_options (question_id, option_text, is_correct, order_num) VALUES
(7, 'A unique identifier for each record', TRUE, 1),
(7, 'A key that opens the database', FALSE, 2),
(7, 'The first key in a Table', FALSE, 3),
(7, 'A composite of all columns', FALSE, 4),
(8, '3NF (Third Normal Form)', TRUE, 1),
(8, '1NF (First Normal Form)', FALSE, 2),
(8, '2NF (Second Normal Form)', FALSE, 3),
(8, 'BCNF (Boyce-Codd Normal Form)', FALSE, 4),
(9, 'Atomicity', TRUE, 1),
(9, 'Consistency', TRUE, 2),
(9, 'Isolation', TRUE, 3),
(9, 'Durability', TRUE, 4),
(9, 'Integrity', FALSE, 5),
(10, 'A reference to primary key in another table', TRUE, 1),
(10, 'A key that is not primary', FALSE, 2),
(10, 'An external password', FALSE, 3),
(10, 'A composite key', FALSE, 4),
(11, 'SELECT', TRUE, 1),
(11, 'GET', FALSE, 2),
(11, 'RETRIEVE', FALSE, 3),
(11, 'FETCH', FALSE, 4);

INSERT INTO questions (test_id, question_text, question_type, order_num) VALUES
(3, 'What does HTML stand for?', 'single', 1),
(3, 'Which CSS property changes text color?', 'single', 2),
(3, 'Which HTML tag is used for JavaScript?', 'single', 3),
(3, 'What is the box model in CSS? (Select all that apply)', 'multiple', 4),
(3, 'Which event occurs when user clicks an element?', 'single', 5);

INSERT INTO answer_options (question_id, option_text, is_correct, order_num) VALUES
(12, 'HyperText Markup Language', TRUE, 1),
(12, 'High Tech Modern Language', FALSE, 2),
(12, 'Hyper Transfer Markup Language', FALSE, 3),
(12, 'Home Tool Markup Language', FALSE, 4),
(13, 'color', TRUE, 1),
(13, 'text-color', FALSE, 2),
(13, 'font-color', FALSE, 3),
(13, 'foreground', FALSE, 4),
(14, '<script>', TRUE, 1),
(14, '<javascript>', FALSE, 2),
(14, '<js>', FALSE, 3),
(14, '<code>', FALSE, 4),
(15, 'Content', TRUE, 1),
(15, 'Padding', TRUE, 2),
(15, 'Border', TRUE, 3),
(15, 'Margin', TRUE, 4),
(15, 'Outline', FALSE, 5),
(16, 'onclick', TRUE, 1),
(16, 'onmouseclick', FALSE, 2),
(16, 'onchange', FALSE, 3),
(16, 'onhover', FALSE, 4);

INSERT INTO questions (test_id, question_text, question_type, order_num) VALUES
(4, 'What is unit testing?', 'single', 1),
(4, 'Which testing level comes first?', 'single', 2),
(4, 'What is regression testing? (Select all that apply)', 'multiple', 3),
(4, 'What does TDD stand for?', 'single', 4),
(4, 'Which is a black-box testing technique?', 'single', 5);

INSERT INTO answer_options (question_id, option_text, is_correct, order_num) VALUES
(17, 'Testing individual components', TRUE, 1),
(17, 'Testing the whole system', FALSE, 2),
(17, 'Testing user interface', FALSE, 3),
(17, 'Testing database only', FALSE, 4),
(18, 'Unit Testing', TRUE, 1),
(18, 'Integration Testing', FALSE, 2),
(18, 'System Testing', FALSE, 3),
(18, 'Acceptance Testing', FALSE, 4),
(19, 'Testing after code changes', TRUE, 1),
(19, 'Testing new features only', FALSE, 2),
(19, 'Ensuring existing functionality works', TRUE, 3),
(19, 'Testing performance', FALSE, 4),
(20, 'Test-Driven Development', TRUE, 1),
(20, 'Test Design Document', FALSE, 2),
(20, 'Technical Design Description', FALSE, 3),
(20, 'Total Development Delivery', FALSE, 4),
(21, 'Equivalence Partitioning', TRUE, 1),
(21, 'Statement Coverage', FALSE, 2),
(21, 'Path Testing', FALSE, 3),
(21, 'Branch Coverage', FALSE, 4);

INSERT INTO test_assignments (user_id, test_id, assigned_date, deadline, attempts_left, status) VALUES
(3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 2, 'assigned'),
(3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '10 days', 1, 'assigned'),
(3, 3, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 0, 'completed'),
(4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '5 days', 3, 'assigned'),
(4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '14 days', 1, 'assigned'),
(5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 2, 'assigned'),
(5, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 1, 'in_progress'),
(6, 1, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '3 days', 0, 'expired'),
(6, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', 2, 'assigned');

INSERT INTO test_results (user_id, test_id, start_time, end_time, score_percent, is_passed, attempts_used, answers_data) VALUES
(3, 3, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '25 minutes', 85.00, TRUE, 1, '{"answers": {"12": 1, "13": 1, "14": 1, "15": [1,2,3,4], "16": 1}}'),
(3, 1, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '8 days' + INTERVAL '28 minutes', 65.00, FALSE, 1, '{"answers": {"1": 1, "2": 1, "3": [1,3], "4": 1, "5": 1, "6": [1,3]}}'),
(6, 1, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '30 minutes', 55.00, FALSE, 1, '{"answers": {"1": 1, "2": 2, "3": [1], "4": 1, "5": 2, "6": [1]}}');
