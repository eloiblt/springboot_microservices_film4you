CREATE SCHEMA IF NOT EXISTS public;

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 5000;

CREATE TABLE IF NOT EXISTS public.user
(
    id        bigserial NOT NULL,
    lastname  character varying(255),
    firstname character varying(255),
    email     character varying(255) UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE INDEX user_email ON public.user(email);
