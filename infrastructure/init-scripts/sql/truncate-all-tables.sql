-- Empties every table of the family and grandparents modules, to start from a clean slate.
-- The tables themselves stay; only their rows go. Run it in a SQL window on the kdg database.
-- It looks the tables up, so it works on every branch, whichever tables that branch uses.
--
-- Deliberately not in init-scripts/sql: that folder runs when the database is created.

DO
$$
    DECLARE
        tables TEXT;
    BEGIN
        SELECT string_agg(format('%I.%I', schemaname, tablename), ', ')
        INTO tables
        FROM pg_tables
        WHERE schemaname IN ('kdg_family', 'kdg_grandparents');

        IF tables IS NOT NULL THEN
            RAISE NOTICE 'Truncating %', tables;
            EXECUTE 'TRUNCATE TABLE ' || tables || ' CASCADE';
        END IF;
    END
$$;
