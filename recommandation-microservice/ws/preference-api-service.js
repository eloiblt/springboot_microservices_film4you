const axios = require('axios');
const loadBalancer = require('../utils/load-balancer');

const getPreferencesFromCurrentUser = async (id) => {
    try {
        const address = loadBalancer.getAdresseToCall('preferences');
        const response = await axios({
            method: 'get',
            url: `http://${address.ip}:${address.port}/private/user/${id}`,
            headers: {
                'Content-Type': 'application/json'
            }            
        });
        return response.data;
    } catch (e) {
        console.log(`[-] Erreur lors de la requête pour retourner les préférences de l'utilisateur courant`, e);
        return null;
    }

}

module.exports = {
    getPreferencesFromCurrentUser,
}
