-- Создание таблицы STUDENT_GROUP
CREATE TABLE STUDENT_GROUP (
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

-- Заполнение таблицы STUDENT_GROUP
INSERT INTO STUDENT_GROUP (name) VALUES
('ИПМбд-01-21'),
('ИУСбд-01-21'),
('ИПМбд-01-22');


-- Заполнение таблицы STUDENT
INSERT INTO STUDENT (first_name, patronymic, last_name, student_group_id, email, student_card_number, tg_chat_id) VALUES
('Елизавета', 'Алексеевна', 'Кутузова', 1, '111@pfur.ru', '111', 780422689)

