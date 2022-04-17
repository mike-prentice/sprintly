import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Stats from './stats';
import Trends from './trends';
import Ranking from './ranking';
import Map from './map';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}stats`} component={Stats} />
        <ErrorBoundaryRoute path={`${match.url}trends`} component={Trends} />
        <ErrorBoundaryRoute path={`${match.url}ranking`} component={Ranking} />
        <ErrorBoundaryRoute path={`${match.url}map`} component={Map} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
