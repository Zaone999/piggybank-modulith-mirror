CREATE USER kdg WITH PASSWORD 'kdg';
CREATE DATABASE kdg;
GRANT ALL PRIVILEGES ON DATABASE kdg TO kdg;

\c kdg kdg;
CREATE SCHEMA kdg_family;
CREATE SCHEMA kdg_grandparents;


CREATE TABLE kdg_family.piggybank (
                           uuid UUID PRIMARY KEY,
                           owner UUID NOT NULL,
                           balance NUMERIC(19, 2) NOT NULL
);



CREATE TABLE kdg_grandparents.savings_projection (
                                      owner UUID PRIMARY KEY,
                                      balance NUMERIC(19, 2) NOT NULL
);