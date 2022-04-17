import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ranking from './ranking';
import RankingDetail from './ranking-detail';
import RankingUpdate from './ranking-update';
import RankingDeleteDialog from './ranking-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RankingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RankingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RankingDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ranking} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RankingDeleteDialog} />
  </>
);

export default Routes;
