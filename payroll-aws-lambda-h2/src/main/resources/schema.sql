DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    id              BINARY VARYING(500) PRIMARY KEY,
    employee_id     varchar(250)        NOT NULL,
    first_name      varchar(100)        NOT NULL,
    last_name       varchar(100)        NOT NULL,
    middle_name     varchar(100),
    email           varchar(250)        NOT NULL,
    employee_type   varchar(100)        NOT NULL default 'FULL-TIME',
    sal_amount      number              NOT NULL,
    sal_currency    varchar(250)        NOT NULL,
    creation_date   timestamp           NOT NULL default current_timestamp,
    version         BIGINT             
);

CREATE UNIQUE INDEX UN_EMP_ID_IDX 
ON employee(employee_id);

CREATE UNIQUE INDEX UN_EMP_EMAIL_IDX 
ON employee(email);