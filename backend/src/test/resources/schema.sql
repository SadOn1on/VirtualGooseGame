DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS T_GOOSE;
DROP TABLE IF EXISTS T_ITEM;
DROP TABLE IF EXISTS users;

-- Drop existing sequence if it exists
DROP SEQUENCE IF EXISTS t_goose_seq;
DROP SEQUENCE IF EXISTS t_item_seq;

-- Create sequence for T_GOOSE primary key
CREATE SEQUENCE t_goose_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE t_item_seq START WITH 1 INCREMENT BY 1;

-- Table creation for 'users'
CREATE TABLE users
(
    username varchar_ignorecase(50)  NOT NULL PRIMARY KEY,
    password varchar_ignorecase(500) NOT NULL,
    enabled  BOOLEAN NOT NULL
);

-- Table creation for 'authorities'
CREATE TABLE authorities
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authority_user FOREIGN KEY (username) REFERENCES users (username)
);

create unique index ix_auth_username on authorities (username, authority);

-- Table creation for 'T_GOOSE'
CREATE TABLE T_GOOSE
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    goose_name VARCHAR(255),
    age        INT,
    is_ill     BOOLEAN,
    health     INT,
    hunger     INT,
    username   VARCHAR(50) NOT NULL,
    CONSTRAINT fk_goose_user FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);

-- Table creation for 'T_ITEM'
CREATE TABLE T_ITEM
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name       VARCHAR(255),
    health_value    INT,
    nutrition_value INT,
    is_infected     BOOLEAN,
    username        VARCHAR(50) NOT NULL,
    CONSTRAINT fk_item_user FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
);