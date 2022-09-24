const getBestRatedClusterFromPreferences = (films) => {
    let clusters = [];
    try {
        films.forEach(f => {
            // Create a new object in array for each cluster to stock the cluster infos
            if (!clusters.some(c => c.id === f.cluster_id)) {
                clusters.push({'id': f.cluster_id, markSum: 0, average: 0, occurences: 0});
            }

            const cluster = clusters.find(c => c.id === f.cluster_id);

            cluster.occurences += 1;
            cluster.marksSum += f.mark;
        });

        let maximumCluster;
        // Calculate each cluster average from its values
        clusters.forEach(c => {
            c.average = c.marksSum / c.occurences;
            if (!maximumCluster) {
                maximumCluster = c;
            } else if (maximumCluster.average < c.average) {
                maximumCluster = c.average;
            }
        });

        return maximumCluster.id;
    } catch (e) {
        console.log(`[-] Erreur lors de la recherche du meilleur cluster`, e);
        return null;
    }
}

module.exports = {
    getBestRatedClusterFromPreferences,
}
