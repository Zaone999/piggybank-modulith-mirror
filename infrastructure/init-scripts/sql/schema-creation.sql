CREATE USER kdg WITH PASSWORD 'kdg';
CREATE DATABASE kdg;
GRANT ALL PRIVILEGES ON DATABASE kdg TO kdg;

\c kdg kdg;
CREATE SCHEMA kdg_family;
CREATE SCHEMA kdg_grandparents;


CREATE TABLE kdg_family.family_member (
                           uuid       UUID         PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name  VARCHAR(255) NOT NULL
);

-- the event store of the piggybanks: one row per event, only ever inserted
CREATE TABLE kdg_family.piggybank_event (
                           event_id      UUID           PRIMARY KEY,
                           piggybank_id  UUID           NOT NULL,
                           owner         UUID           NOT NULL,
                           type          VARCHAR(20)    NOT NULL,
                           amount        NUMERIC(19, 2),
                           point_in_time TIMESTAMP      NOT NULL
);

-- one piggybank per owner: only one CREATED event per owner
CREATE UNIQUE INDEX piggybank_event_one_created_per_owner
    ON kdg_family.piggybank_event (owner) WHERE type = 'CREATED';



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

CREATE TABLE kdg_grandparents.grandchild (
                           uuid       UUID         PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name  VARCHAR(255) NOT NULL
);
