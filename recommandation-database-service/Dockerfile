FROM postgres:13.2-alpine

ENV PGDATA="/opt/postgresql"
WORKDIR $PGDATA

COPY db/initdb.sql /docker-entrypoint-initdb.d/
