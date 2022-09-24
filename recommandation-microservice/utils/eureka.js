const uuid = require('uuid');
const Eureka = require('eureka-js-client').Eureka;
const dns = require('dns');
const fs = require('fs');

let client = null;

const instanceId = uuid.v4();
const applicationName = process.env.APPLICATION_NAME || 'localhost';

const isFromDocker = process.env.IS_FROM_DOCKER || false;


function getHostname() {
    if(isFromDocker) {
        return fs.readFileSync('/etc/hostname', 'utf8').trim();
    } else {
        return 'localhost';
    }
}

function getIp() {
    return new Promise(((resolve, reject) => {
        if(applicationName === 'localhost') {
            resolve('127.0.0.1');
        } else {
            dns.lookup(applicationName, (err, address) => {
                if (err) {
                    console.error('[+] Erreur resolve dns : ', err);
                    reject(err);
                } else {
                    console.log('[+] address', address);
                    resolve(address);
                }
            });
        }
    }))
}

function startEureka(eurekaClient) {
    return new Promise(((resolve) => {
        eurekaClient.start((error) => {
            if (error) {
                console.log('[+] Connection error : ', error);
                console.log('[+] Try again');
                startEureka(eurekaClient).then(() => {
                    resolve();
                });
            } else {
                resolve();
                console.log('[+] Eureka connection complete !');
            }
        });
    }))
}

async function activeEureka(currentPort) {
    return new Promise((resolve) => {
        getIp().then((ip) => {
            const hostname = getHostname();

            const configEureka = {
                // application instance information
                instance: {
                    app: 'recommandations',
                    hostName: hostname,
                    ipAddr: ip,
                    instanceId: `recommandations:${instanceId}`,
                    port: {
                        '$': currentPort,
                        '@enabled': 'true',
                    },
                    vipAddress: `recommandations`,
                    statusPageUrl: `http://${hostname}:${currentPort}/actuator/info`,
                    healthCheckUrl: `http://${hostname}:${currentPort}/actuator/health`,
                    homePageUrl: `http://${hostname}:${currentPort}/`,
                    dataCenterInfo: {
                        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                        name: 'MyOwn',
                    },
                    registerWithEureka: true,
                    fetchRegistry: true,
                    metadata: {
                        "metrics.path": "/metrics"
                    }
                },
                eureka: {
                    // eureka server host / port
                    host: process.env.EUREKA_HOST || 'localhost',
                    port: process.env.EUREKA_PORT || 8010,
                    servicePath: '/eureka/apps'
                },
            }

            console.log(configEureka);

            client = new Eureka(configEureka);

            client.logger.level('debug');
            startEureka(client).then(() => {
                resolve();
            });
        });
    });
}

function getClient() {
    return client;
}

// LISTEN FOR KILL PROCESS
function exitHandler(options, exitCode) {
    if (options.cleanup) console.log('[+] Clean.');
    if (exitCode || exitCode === 0) console.log(`[+] Exit code : ${exitCode}`);
    if (options.exit) {
        client?.stop(function () {
            process.exit();
        });
    }
}

//do something when app is closing
process.on('exit', exitHandler.bind(null, {cleanup: true}));

//catches ctrl+c event
process.on('SIGINT', exitHandler.bind(null, {exit: true}));

// catches "kill pid" (for example: nodemon restart)
process.on('SIGUSR1', exitHandler.bind(null, {exit: true}));
process.on('SIGUSR2', exitHandler.bind(null, {exit: true}));

//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind(null, {exit: true}));

module.exports = {
    getClient,
    activeEureka
}
