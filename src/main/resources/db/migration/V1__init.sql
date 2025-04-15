-- Создание таблицы STUDENT_GROUP
CREATE TABLE STUDENT_GROUP (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Создание таблицы ASSIGNMENT_GROUP
CREATE TABLE ASSIGNMENT_GROUP (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Создание таблицы TEACHER
CREATE TABLE TEACHER (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tg_chat_id BIGINT NOT NULL UNIQUE
);

-- Создание таблицы STUDENT
CREATE TABLE STUDENT (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    student_group_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    student_card_number VARCHAR(255) NOT NULL UNIQUE,
    tg_chat_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_student_group FOREIGN KEY (student_group_id) REFERENCES STUDENT_GROUP (id) ON DELETE CASCADE
);

-- Создание таблицы TASK
CREATE TABLE TASK (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    teacher_id BIGINT NOT NULL,
    assignment_group_id BIGINT NOT NULL,
    CONSTRAINT fk_task_teacher FOREIGN KEY (teacher_id) REFERENCES TEACHER(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignment_group FOREIGN KEY (assignment_group_id) REFERENCES ASSIGNMENT_GROUP(id) ON DELETE CASCADE
);

-- Создание таблицы TEST
CREATE TABLE TEST (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    script TEXT NOT NULL,
    task_id BIGINT NOT NULL,
    CONSTRAINT fk_test_task FOREIGN KEY (task_id) REFERENCES TASK(id) ON DELETE CASCADE
);

CREATE TABLE TASK_STUDENT_GROUP (
    task_id BIGINT NOT NULL,
    student_group_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, student_group_id),
    CONSTRAINT fk_task_student_group_task FOREIGN KEY (task_id) REFERENCES TASK(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_student_group_group FOREIGN KEY (student_group_id) REFERENCES STUDENT_GROUP(id) ON DELETE CASCADE
);

-- Создание таблицы SUBMISSION
CREATE TABLE SUBMISSION (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    script TEXT NOT NULL,
    task_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    CONSTRAINT fk_submission_task FOREIGN KEY (task_id) REFERENCES TASK(id) ON DELETE CASCADE,
    CONSTRAINT fk_submission_student FOREIGN KEY (student_id) REFERENCES STUDENT(id) ON DELETE CASCADE,
    CONSTRAINT uq_submission_unique UNIQUE (student_id, task_id)
);

CREATE TABLE STATUS_TEST_RESULT (
    status VARCHAR(32) PRIMARY KEY
);

-- Создание таблицы TEST_RESULT
CREATE TABLE TEST_RESULT (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    log TEXT,
    status VARCHAR(32) NOT NULL,
    test_id BIGINT NOT NULL,
    submission_id BIGINT NOT NULL,
    CONSTRAINT fk_result_test FOREIGN KEY (test_id) REFERENCES TEST(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_submission FOREIGN KEY (submission_id) REFERENCES SUBMISSION(id) ON DELETE CASCADE,
    CONSTRAINT fk_result_status FOREIGN KEY (status) REFERENCES STATUS_TEST_RESULT(status),
    CONSTRAINT uq_test_result UNIQUE (test_id, submission_id)
);

INSERT INTO STATUS_TEST_RESULT (status) VALUES
('SUCCESS'),
('FAILURE');

-- Заполнение таблицы STUDENT_GROUP
INSERT INTO STUDENT_GROUP (name) VALUES
('ИПМбд-01-21'),
('ИУСбд-01-21'),
('ИПМбд-01-22');

-- Заполнение таблицы ASSIGNMENT_GROUP
INSERT INTO ASSIGNMENT_GROUP (name) VALUES
('МКП_ИПМбд-01-21'),
('Программирование 3 курс'),
('golang 3 семестр');

-- Заполнение таблицы STUDENT
INSERT INTO STUDENT (first_name, patronymic, last_name, student_group_id, email, student_card_number, tg_chat_id) VALUES
('Елизавета', 'Алексеевна', 'Кутузова', 1, '111@pfur.ru', '111', 780422689);

-- Заполнение таблицы TEACHER
INSERT INTO TEACHER (first_name, patronymic, last_name, email, tg_chat_id) VALUES
('Роман', 'Андреевич', 'Самохин', '111@pfur.ru', 711118563);
