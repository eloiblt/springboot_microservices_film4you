const axios = require('axios');
const loadBalancer = require('../utils/load-balancer');

const collectUserFromJwtToken = async (token) => {
    try {
        const address = loadBalancer.getAdresseToCall('users');
        const response = await axios.post(`http://${address.ip}:${address.port}/private/auth/user`, token);
        return response.data;
    } catch (e) {
        console.log('[-] Erreur lors de la requête pour retourner l\'utilisateur', e);
        return null;
    }
}

module.exports = {
    collectUserFromJwtToken,
}
