DROP TABLE IF EXISTS public.watch;
CREATE TABLE public.watch
(
    id        bigserial NOT NULL,
    user_id bigint not null,
    film_id character varying(255) not null,
    CONSTRAINT pk_watch PRIMARY KEY (id)
);

CREATE INDEX watch_user_id ON watch(user_id);
CREATE INDEX watch_film_id ON watch(film_id);
