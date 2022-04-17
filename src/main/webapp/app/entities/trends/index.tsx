import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Trends from './trends';
import TrendsDetail from './trends-detail';
import TrendsUpdate from './trends-update';
import TrendsDeleteDialog from './trends-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TrendsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TrendsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TrendsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Trends} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TrendsDeleteDialog} />
  </>
);

export default Routes;
