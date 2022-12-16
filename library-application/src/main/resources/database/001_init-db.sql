--liquibase formatted sql
--changeset Krystian:1 create Author table
CREATE TABLE if NOT EXISTS Author
(
    id
    int
    NOT
    NULL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    20
) NOT NULL,
    surname VARCHAR
(
    20
) NOT NULL
    );
--rollback drop table Author;

--changeset Krystian:2 create Book table
CREATE TABLE if NOT EXISTS Book
(
    id
    int
    PRIMARY
    KEY,
    title
    VARCHAR
(
    20
) NOT NULL,
    author_Id int REFERENCES Author
(
    id
) NOT NULL,
    quantity int NOT NULL,
    quantity_Avaible int NOT NULL
    );

--rollback drop table Book;

--changeset Krystian:3 create table Reader
CREATE TABLE if NOT EXISTS Reader
(
    id
    int
    PRIMARY
    KEY,
    name
    VARCHAR
(
    20
) NOT NULL,
    surname VARCHAR
(
    20
) NOT NULL
    );

--rollback drop table Reader;

--changeset Krystian:4 create table Parent
CREATE TABLE if NOT EXISTS Parent
(
    id int REFERENCES Reader
(
    id
) PRIMARY KEY,
    address VARCHAR
(
    20
) NOT NULL,
    phone_number VARCHAR
(
    10
) NOT NULL
    );

--rollback drop table Parent;

--changeset Krystian:5 create table Child;
CREATE TABLE if NOT EXISTS Child
(
    id int REFERENCES Reader
(
    id
) PRIMARY KEY,
    parent_id int REFERENCES Parent
(
    id
)
    );

--rollback drop table Child;

--changeset Krystian:6 create table Loans;

CREATE TABLE if NOT EXISTS Loans
(
    book_id
    int
    NOT
    NULL,
    reader_id
    int
    NOT
    NULL,
    FOREIGN
    KEY
(
    book_id
) REFERENCES BOOK
(
    id
),
    FOREIGN KEY
(
    reader_id
) REFERENCES READER
(
    id
)
    );
--rollback drop table Loans;

--changeset Krystian:7 insert column when borrowed;
ALTER TABLE Loans
    ADD COLUMN BorrowDate DATE DEFAULT CURRENT_DATE;
--rollback ALTER TABLE Loans DROP COLUMN BorrowDate;

--changeset Krystian:8 insert column when returned book;
ALTER TABLE Loans
    ADD COLUMN ReturnedDate DATE NULL;
--rollback ALTER TABLE Loans DROP COLUMN ReturnedDate;

--changeset Krystian:9 remove column quantity_avaible from book;
ALTER TABLE book
DROP
COLUMN quantity_Avaible;

--rollback ALTER TABLE book ADD quantity_Avaible int;

--changeset Krystian:10 set default value to BorrowDate;
ALTER TABLE Loans
    ALTER COLUMN BorrowDate SET DEFAULT CURRENT_DATE;
--rollback ALTER TABLE loans ALTER COLUMN BorrowDate SET DEFAULT NULL;
--changeset Krystian:11 add birth date column to child;
ALTER TABLE reader
    ADD COLUMN birth_date DATE NULL;
--rollback ALTER TABLE reader DROP COLUMN birth_date;
--changeset Krystian:12 add configuration table;
CREATE TABLE if NOT EXISTS Configuration
(
    id
    int
    PRIMARY
    KEY
    NOT
    NULL,
    maxNumberOfBorrowedBooks
    int
    NOT
    NULL,
    maxNumberOfDaysToBorrowABook
    int
    NOT
    NULL,
    minAgeToBorrowABook
    int
    NOT
    NULL
);
--rollback DROP TABLE Configuration;
--changeset Krystian:13 add unique value of table author;
ALTER TABLE author
    ADD CONSTRAINT UC_author UNIQUE (name, surname);
--rollback ALTER TABLE author DROP CONSTRAINT UC_author

--changeset Krystian:14 add primary key to loans
ALTER TABLE loans
ADD constraint key primary key  (book_id,reader_id,borrowdate);
--rollback Alter table loans drop constraint key;

--changeset Krystian:15 change column name in loans
ALTER TABLE Loans
rename column ReturnedDate to returned_date;
--rollback ALTER TABLE Loans rename column returned_date to ReturnedDate;

--changeset Krystian:16 rename borrowdate column in loans
ALTER TABLE Loans
rename column BorrowDate to borrow_date;
--rollback ALTER TABLE Loans rename column borrow_date to BorrowDate;

--changeset Krystian:17 set default loans rules
INSERT INTO Configuration values (1,10,10,10);
--rollback delete from configuration where id = 1;