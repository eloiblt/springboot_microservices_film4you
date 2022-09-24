require('dotenv').config();
const app = require('express')();
const server = require('http').createServer(app);
const eureka = require('./utils/eureka');
const clusterLib = require('./utils/cluster');
const userApi = require('./ws/user-api-service');
const filmApi = require('./ws/film-api-service');
const preferenceApi = require('./ws/preference-api-service');
const pgDriver = require('./db/pg-driver');
const actuator = require('express-actuator');
const promBundle = require("express-prom-bundle");

const metricsMiddleware = promBundle({
    includeMethod: true,
    includePath: true,
    includeStatusCode: true,
    includeUp: true,
    customLabels: {project_name: 'recommandations-api-service', project_type: 'test_metrics_labels'},
    promClient: {
        collectDefaultMetrics: {
        }
    }
});
// add the prometheus middleware to all routes
app.use(metricsMiddleware);

// Active Actuator on NodeJS App
app.use(actuator({
    basePath: '/actuator'
}));

app.use('/secured/**', async (req, res, next) => {

    const userToken = req.header('Authorization');

    if(!userToken) {
        res.send(403, 'Forbidden');
        next();
        return;
    }

    console.log('[+] Fetch user');
    const user = await userApi.collectUserFromJwtToken(userToken.replace('Bearer ', ''));

    if(!user) {
        res.send(403, 'Forbidden');
        next();
        return;
    }

    console.log('[+] Add user to request');

    req.currentUser = user;
    next();
});

// example configuration
app.get('/public/ping', (req, res) => {
    console.log('[+] Pong !');
    res.json({ message: 'pong', port: server.address().port })
});

// Getting similar films from film id
app.get('/secured/lookalike/:id', async (req, res) => {
    const id = req.params.id;
    console.log(`[+] Getting similar films from ${id}`);
    const result = await pgDriver.getSimilarFilmsIds(id);
    const filmsIds = result?.map(p => p.film_id) || [];
    const films = (filmsIds.length > 0) ? await filmApi.getFilmsFromIds(filmsIds) : [];
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(films));
});

// Getting recommandation films from preferences
app.get('/secured/recommandation', async (req, res) => {
    res.setHeader('Content-Type', 'application/json');
    const preferences = await preferenceApi.getPreferencesFromCurrentUser(req.currentUser.id);
    const preferencesIds = preferences?.map(p => p.filmId) || [];

    if (preferencesIds.length === 0) {
        res.send(JSON.stringify([]));
        return;
    }

    const clusteredFilms = await pgDriver.getClustersFromPreferences(preferencesIds);
    // Merge two arrays
    let clusteredPreferences = clusteredFilms.map(f => ({
        ...preferences.find((p) => (p.filmId === f.film_id) && p),
        ...f
    }))
    // Remove useless attributes
    clusteredPreferences = clusteredPreferences.map(function (item) {
        delete item.id;
        delete item.userId;
        delete item.filmId;
        return item;
    });
    // Get cluster from which pick random films
    const cluster = clusterLib.getBestRatedClusterFromPreferences(clusteredPreferences);
    // Get random set of films from this cluster

    // Removing films that we already have in preferences from list of films of recommended cluster
    const preferencesForRequest = clusteredPreferences.filter(p => p.cluster_id === cluster).map(a => a.film_id);
    const result = await pgDriver.getFilmsFromCluster(cluster, preferencesForRequest);
    const filmsIds = result?.map(p => p.film_id) || [];
    const films = await filmApi.getFilmsFromIds(filmsIds);
    res.send(JSON.stringify(films));
});

server.listen(process.env.SERVER_PORT || 0, async () => {
    console.log(`[+] Server started on port : ${server.address().port}`);
    await eureka.activeEureka(server.address().port);
});

module.exports = server
