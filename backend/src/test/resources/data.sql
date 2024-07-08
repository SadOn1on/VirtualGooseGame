-- Insert sample data into 'users' table
INSERT INTO users (username, password, enabled) VALUES ('alice', '{noop}password1', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('bob', '{noop}password2', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('carol', '{noop}password1', FALSE);
INSERT INTO users (username, password, enabled) VALUES ('dave', '{noop}password2', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('eve', '{noop}password1', TRUE);

-- Insert sample data into 'authorities' table
INSERT INTO authorities (username, authority) VALUES ('alice', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('alice', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('bob', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('carol', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('dave', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('eve', 'ROLE_USER');

-- Insert sample data into 'T_GOOSE' table
INSERT INTO T_GOOSE (id, goose_name, age, is_ill, health, hunger, username) VALUES (nextval('t_goose_seq'), 'Goose1', 2, FALSE, 100, 20, 'alice');
INSERT INTO T_GOOSE (id, goose_name, age, is_ill, health, hunger, username) VALUES (nextval('t_goose_seq'), 'Goose3', 1, FALSE, 90, 10, 'bob');
INSERT INTO T_GOOSE (id, goose_name, age, is_ill, health, hunger, username) VALUES (nextval('t_goose_seq'), 'Goose4', 4, FALSE, 80, 50, 'dave');
INSERT INTO T_GOOSE (id, goose_name, age, is_ill, health, hunger, username) VALUES (nextval('t_goose_seq'), 'Goose5', 2, TRUE, 60, 40, 'eve');

-- Insert sample data into 'T_ITEM' table
INSERT INTO T_ITEM (id, item_name, health_value, nutrition_value, is_infected, username) VALUES (nextval('t_item_seq'), 'Item1', 10, 20, FALSE, 'alice');
INSERT INTO T_ITEM (id, item_name, health_value, nutrition_value, is_infected, username) VALUES (nextval('t_item_seq'), 'Item2', 15, 25, TRUE, 'alice');
INSERT INTO T_ITEM (id, item_name, health_value, nutrition_value, is_infected, username) VALUES (nextval('t_item_seq'), 'Item3', 20, 30, FALSE, 'bob');
INSERT INTO T_ITEM (id, item_name, health_value, nutrition_value, is_infected, username) VALUES (nextval('t_item_seq'), 'Item4', 25, 35, TRUE, 'dave');
INSERT INTO T_ITEM (id, item_name, health_value, nutrition_value, is_infected, username) VALUES (nextval('t_item_seq'), 'Item5', 30, 40, FALSE, 'eve');

