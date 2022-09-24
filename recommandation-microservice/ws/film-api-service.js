const axios = require('axios');
const loadBalancer = require('../utils/load-balancer');

const getFilmsFromIds = async (filmsIds) => {
    try {
        const address = loadBalancer.getAdresseToCall('films');
        const response = await axios({
            method: 'post',
            url: `http://${address.ip}:${address.port}/private/list`,
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(filmsIds)
        });
        return response.data;
    } catch (e) {
        console.log('[-] Erreur lors de la requête pour retourner les films à partir de leur id', e);
        return null;
    }
}

module.exports = {
    getFilmsFromIds,
}
