# Film4U

## Membres du groupe

- BELLET Éloi
- BELDJILALI Iliès
- FOLLÉAS Brice
- MITTELETTE Nathan

## Sonar

### Frontend Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-frontend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=film4u-frontend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-frontend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=film4u-frontend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-frontend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=film4u-frontend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-frontend&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=film4u-frontend)


### Maven Microservice

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-maven&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=film4u-maven)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-maven&metric=bugs)](https://sonarcloud.io/summary/new_code?id=film4u-maven)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-maven&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=film4u-maven)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-maven&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=film4u-maven)

### Recommandation Microservice

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-recommandation-microservice&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=film4u-recommandation-microservice)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-recommandation-microservice&metric=bugs)](https://sonarcloud.io/summary/new_code?id=film4u-recommandation-microservice)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-recommandation-microservice&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=film4u-recommandation-microservice)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-recommandation-microservice&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=film4u-recommandation-microservice)

### Film Database Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-database-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=film4u-film-database-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-database-service&metric=bugs)](https://sonarcloud.io/summary/new_code?id=film4u-film-database-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-database-service&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=film4u-film-database-service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-database-service&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=film4u-film-database-service)

### Clustering Module

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-clustering-module&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=film4u-film-clustering-module)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-clustering-module&metric=bugs)](https://sonarcloud.io/summary/new_code?id=film4u-film-clustering-module)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-clustering-module&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=film4u-film-clustering-module)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=film4u-film-clustering-module&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=film4u-film-clustering-module)

## Documentation

Pour chaque microservice maven swagger est intégré. 

Il faut passer par l'API Gateway pour y accéder : `URL_API_GATEWAY/MICROSERVICE_NAME/swagger-ui/index.html`.

## Installation 

### Pré requis 

- Installation de `maven`
- Installation de `docker`


### Compilation des projets maven

À la racine du projet lancer cette commande pour générer les fichiers Jar des microservices.

```bash
mvn clean install -DskipTests
```

### Lancement de docker

Un fichier `docker-compose.yml` se trouve à la racine du projet. 

Lancement de la commande Docker compose.

```bash
docker-compose up --build -d
```

### Liste des éléments lancés

- Microservice User (Java)
- Microservice Film (Java)
- Microservice Préférence (Java)
- Microservice Recommandation (Node)
- Base de données User (Postgres)
- Base de données Film (Mongo)
- Base de données Préférence (Postgres)
- Base de données Recommandation (Postgres)
- Api Gateway (Java)
- Discovery Service (Java)
- Web (Angular)
