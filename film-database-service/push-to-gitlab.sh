echo [+] Check that the container is not running
docker compose down
echo [+] Start the container
docker compose up --build -d
echo [+] Press enter when migration are done.
read pressEnter
docker exec -u root film-database-service rm -rf /docker-entrypoint-initdb.d/
echo [+] Commit current image
docker commit film-database-service registry.gitlab.com/nathan-mittelette/film4you/film-database-service:1.0.0
docker push registry.gitlab.com/nathan-mittelette/film4you/film-database-service:1.0.0
