const Pool = require("pg").Pool;

const pool = new Pool({
  user: process.env.PGUSER,
  host: process.env.PGHOST,
  database: process.env.PGDATABASE,
  password: process.env.PGPASSWORD,
  port: process.env.PGPORT
});

const getSimilarFilmsIds = async (id) => {
  const q = 'select r1.film_id, r1.cluster_id, r1.mark from recommandation r1 left join recommandation r2 on r2.film_id = $1 where r1.cluster_id = r2.cluster_id and r1.mark > 6 and r1.film_id != r2.film_id order by abs(r2.distance - r1.distance) limit 20';
  try {
    const result = await pool.query(q, [id]);
    return result.rows;
  } catch (e) {
    console.log('[-] Erreur lors de la requête pour retourner les films similaires', e);
    return null;
  }
};

const getClustersFromPreferences = async (ids) => {
  const idsJoin = ids.map((id, index) => `$${index + 1}`).join(',');
  const q = `SELECT film_id, cluster_id, mark
             FROM recommandation
             WHERE film_id in (${idsJoin})`;
  try {
    const res = await pool.query(q, ids);
    return res.rows;
  } catch (e) {
    console.log('[-] Erreur lors de la requête pour retourner les clusters des preferences', e);
    return null;
  }
};

const getFilmsFromCluster = async (cluster, clusteredPreferences) => {
  const idsJoin = clusteredPreferences.map((id, index) => `$${index + 1}`).join(',');
  const q = `SELECT r.film_id FROM (SELECT film_id FROM recommandation WHERE cluster_id = $${clusteredPreferences.length + 1} AND film_id NOT IN (${idsJoin}) AND mark > 6 ORDER BY distance LIMIT 1000) as r ORDER BY RANDOM() LIMIT 20`;
  try {
    const res = await pool.query(q, [...clusteredPreferences, cluster.toString()]);
    return res.rows;
  } catch (e) {
    console.log('[-] Erreur lors de la requête pour retourner les films recommandés', e);
    return null;
  }
}

module.exports = {
  getSimilarFilmsIds,
  getClustersFromPreferences,
  getFilmsFromCluster
};
