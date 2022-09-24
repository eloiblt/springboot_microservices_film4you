const eureka = require("../utils/eureka");

const calls = {};

function saveCall(name, lastIndex) {
    calls[name] = lastIndex;
}

function getCall(name) {
    return calls[name] || 0;
}

function getAdresseToCall(name) {
    if(eureka.getClient() === null) {
        return null;
    }

    const lastIndex = getCall(name);
    const instances = eureka.getClient().getInstancesByAppId(name);

    if(instances.length === 0) {
        return null;
    }

    const nextIndex = (lastIndex + 1) % instances.length;
    saveCall(name, nextIndex);

    return {port: instances[nextIndex].port['$'], ip: instances[nextIndex].ipAddr};
}

module.exports = {
    getAdresseToCall
}
