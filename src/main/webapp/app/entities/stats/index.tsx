import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Stats from './stats';
import StatsDetail from './stats-detail';
import StatsUpdate from './stats-update';
import StatsDeleteDialog from './stats-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StatsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StatsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StatsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Stats} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StatsDeleteDialog} />
  </>
);

export default Routes;
