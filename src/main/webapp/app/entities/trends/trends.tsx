import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrends } from 'app/shared/model/trends.model';
import { getEntities } from './trends.reducer';

export const Trends = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const trendsList = useAppSelector(state => state.trends.entities);
  const loading = useAppSelector(state => state.trends.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="trends-heading" data-cy="TrendsHeading">
        Trends
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/trends/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Trends
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {trendsList && trendsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Avg Pace</th>
                <th>Avgdistance</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {trendsList.map((trends, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/trends/${trends.id}`} color="link" size="sm">
                      {trends.id}
                    </Button>
                  </td>
                  <td>{trends.avgPace}</td>
                  <td>{trends.avgdistance}</td>
                  <td>{trends.user ? trends.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/trends/${trends.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/trends/${trends.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/trends/${trends.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Trends found</div>
        )}
      </div>
    </div>
  );
};

export default Trends;
