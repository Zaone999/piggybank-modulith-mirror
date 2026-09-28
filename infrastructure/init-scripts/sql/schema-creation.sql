CREATE USER kdg WITH PASSWORD 'kdg';
CREATE DATABASE kdg;
GRANT ALL PRIVILEGES ON DATABASE kdg TO kdg;

\c kdg kdg;
CREATE SCHEMA kdg_family;
CREATE SCHEMA kdg_grandparents;


CREATE TABLE kdg_family.piggybank (
                           uuid    UUID           PRIMARY KEY,
                           owner   UUID           NOT NULL UNIQUE,
                           balance NUMERIC(19, 2) NOT NULL
);



CREATE TABLE kdg_grandparents.savings_promise (
                           uuid             UUID           PRIMARY KEY,
                           grandchild       UUID           NOT NULL,
                           balance          NUMERIC(19, 2) NOT NULL,
                           promise_boundary NUMERIC(19, 2) NOT NULL,
                           triggered        BOOLEAN        NOT NULL
);

CREATE TABLE kdg_grandparents.savings_entry (
                           event_id      UUID           PRIMARY KEY,
                           grandchild    UUID           NOT NULL,
                           type          VARCHAR(20)    NOT NULL,
                           amount        NUMERIC(19, 2) NOT NULL,
                           point_in_time TIMESTAMP      NOT NULL
);
