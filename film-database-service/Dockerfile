FROM mongo:5.0.5 as film-database-generate

ENV FILM4U_FILM_DATABASE_GENERATE /opt/film4u-film-db
ENV MONGODB /opt/mongo

RUN mkdir -p $MONGODB \
    && chown -R mongodb:mongodb $MONGODB

# Set the working directory
WORKDIR $FILM4U_FILM_DATABASE_GENERATE

COPY data/movies-cleaned.json $FILM4U_FILM_DATABASE_GENERATE/movies-cleaned.json

COPY bin/1_init-db.js /docker-entrypoint-initdb.d/
COPY bin/2_import-data.sh /docker-entrypoint-initdb.d/

COPY mongodb.conf /etc/mongodb.conf