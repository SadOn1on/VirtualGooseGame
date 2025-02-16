DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS goose;
DROP TABLE IF EXISTS users;

DROP SEQUENCE IF EXISTS t_goose_seq;
DROP SEQUENCE IF EXISTS t_item_seq;

CREATE SEQUENCE t_goose_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE t_item_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    username varchar(50) NOT NULL PRIMARY KEY,
    enabled  BOOLEAN NOT NULL
);

CREATE TABLE goose
(
    id         BIGINT DEFAULT nextval('t_goose_seq') PRIMARY KEY,
    goose_name VARCHAR(255),
    age        INT,
    is_ill     BOOLEAN,
    health     INT,
    hunger     INT,
    username   VARCHAR(50) NOT NULL,
    CONSTRAINT fk_goose_user FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);

CREATE TABLE item
(
    id              BIGINT DEFAULT nextval('t_item_seq') PRIMARY KEY,
    item_name       VARCHAR(255),
    health_value    INT,
    nutrition_value INT,
    is_infected     BOOLEAN,
    username        VARCHAR(50) NOT NULL,
    CONSTRAINT fk_item_user FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);