CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.recommandation
(
    id          bigserial NOT NULL,
    film_id     character varying(255) not null,
    cluster_id  bigint not null,
    mark        numeric not null CHECK (mark >= 0 and mark <= 10),
    CONSTRAINT pk_recommandation PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS recommandation_film_id ON recommandation(film_id);
CREATE INDEX IF NOT EXISTS recommandation_cluster_id ON recommandation(cluster_id);
