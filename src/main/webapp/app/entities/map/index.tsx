import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Map from './map';
import MapDetail from './map-detail';
import MapUpdate from './map-update';
import MapDeleteDialog from './map-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MapUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MapUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MapDetail} />
      <ErrorBoundaryRoute path={match.url} component={Map} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MapDeleteDialog} />
  </>
);

export default Routes;
