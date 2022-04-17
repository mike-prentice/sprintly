import stats from 'app/entities/stats/stats.reducer';
import trends from 'app/entities/trends/trends.reducer';
import ranking from 'app/entities/ranking/ranking.reducer';
import map from 'app/entities/map/map.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  stats,
  trends,
  ranking,
  map,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
