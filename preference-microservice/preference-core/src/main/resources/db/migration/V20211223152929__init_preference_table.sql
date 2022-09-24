CREATE SCHEMA IF NOT EXISTS public;

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 5000;

CREATE TABLE IF NOT EXISTS public.preference
(
    id        bigserial NOT NULL,
    user_id bigint not null,
    film_id character varying(255) not null,
    mark numeric not null CHECK (mark >= 0 and mark <= 10),
    CONSTRAINT pk_preference PRIMARY KEY (id)
);

CREATE INDEX preference_user_id ON preference(user_id);
CREATE INDEX preference_film_id ON preference(film_id);
