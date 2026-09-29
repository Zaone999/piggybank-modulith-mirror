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

CREATE TABLE kdg_grandparents.grandchild (
                           uuid       UUID         PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name  VARCHAR(255) NOT NULL
);


-- The Event Publication Registry of Spring Modulith 2.x: one row per event and listener, written in the
-- publishing transaction. Schema from the Spring Modulith reference, appendix, PostgreSQL. It gets a schema
-- of its own: since PostgreSQL 15 the kdg user may not create tables in public, and application.properties
-- points Hibernate's default schema, used only by this table, at kdg_events.
CREATE SCHEMA kdg_events;

CREATE TABLE kdg_events.event_publication
(
  id                     UUID NOT NULL,
  listener_id            TEXT NOT NULL,
  event_type             TEXT NOT NULL,
  serialized_event       TEXT NOT NULL,
  publication_date       TIMESTAMP WITH TIME ZONE NOT NULL,
  completion_date        TIMESTAMP WITH TIME ZONE,
  status                 TEXT,
  completion_attempts    INT,
  last_resubmission_date TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (id)
);
CREATE INDEX event_publication_serialized_event_hash_idx ON kdg_events.event_publication USING hash(serialized_event);
CREATE INDEX event_publication_by_completion_date_idx ON kdg_events.event_publication (completion_date);
