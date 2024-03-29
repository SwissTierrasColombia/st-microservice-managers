SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA IF NOT EXISTS managers;

SET default_tablespace = '';

CREATE TABLE managers.managers (
    id bigint NOT NULL,
    alias character varying(20),
    created_at timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL,
    tax_identification_number character varying(255) NOT NULL,
    manager_state_id bigint NOT NULL
);

ALTER TABLE managers.managers ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME managers.managers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE managers.managers_profiles (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL
);

ALTER TABLE managers.managers_profiles ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME managers.managers_profiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE managers.managers_states (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE managers.managers_states ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME managers.managers_states_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE managers.managers_users (
    id bigint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    user_code bigint NOT NULL,
    manager_id bigint NOT NULL,
    manager_profile_id bigint NOT NULL
);

ALTER TABLE managers.managers_users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME managers.managers_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

INSERT INTO managers.managers VALUES (1, 'IGAC', '2022-04-06 12:20:11.173', 'INSTITUTO GEOGRÁFICO AGUSTÍN CODAZZI', '8999990049', 1);

INSERT INTO managers.managers_profiles VALUES (1, NULL, 'DIRECTOR');
INSERT INTO managers.managers_profiles VALUES (2, NULL, 'SINIC');

INSERT INTO managers.managers_states VALUES (1, 'ACTIVO');
INSERT INTO managers.managers_states VALUES (2, 'INACTIVO');

SELECT pg_catalog.setval('managers.managers_id_seq', 1, true);
SELECT pg_catalog.setval('managers.managers_profiles_id_seq', 2, true);
SELECT pg_catalog.setval('managers.managers_states_id_seq', 2, true);
SELECT pg_catalog.setval('managers.managers_users_id_seq', 1, false);

ALTER TABLE ONLY managers.managers
    ADD CONSTRAINT managers_pkey PRIMARY KEY (id);

ALTER TABLE ONLY managers.managers_profiles
    ADD CONSTRAINT managers_profiles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY managers.managers_states
    ADD CONSTRAINT managers_states_pkey PRIMARY KEY (id);

ALTER TABLE ONLY managers.managers_users
    ADD CONSTRAINT managers_users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY managers.managers_users
    ADD CONSTRAINT fk63eupshs046f9xlbsk3rb0ad FOREIGN KEY (manager_id) REFERENCES managers.managers(id);

ALTER TABLE ONLY managers.managers_users
    ADD CONSTRAINT fkj1dmw9fd81effm2o89x8nwrwm FOREIGN KEY (manager_profile_id) REFERENCES managers.managers_profiles(id);

ALTER TABLE ONLY managers.managers
    ADD CONSTRAINT fkpvpt8pfl0ls0ei2ywcxb0m79d FOREIGN KEY (manager_state_id) REFERENCES managers.managers_states(id);