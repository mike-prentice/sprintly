import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { LineChart, Line, YAxis, XAxis, AreaChart, Area, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DurationFormat } from 'app/shared/DurationFormat';

import { IStats } from 'app/shared/model/stats.model';
import { getEntities } from './stats.reducer';
import { render } from '@testing-library/react';
// import { duration, distance } from 'app/entities/map/map.js';

export const Stats = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const statsList = useAppSelector(state => state.stats.entities);
  const loading = useAppSelector(state => state.stats.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>

<Card className="jh-card">
    <LineChart width={600} height={400} data={statsList}>
          <Line type="monotone" dataKey="distance" stroke="#8884d8" strokeWidth={2} />
          <Line type="monotone" dataKey="time" stroke="#8984d8" strokeWidth={2}/>
        <YAxis dataKey = "distance"/>
        <XAxis/>
      </LineChart>
      </Card>
      
       <Card className="jh-card">
      <LineChart width={400} height={400} data={statsList}>
        
        <YAxis dataKey = "time"/>
        <XAxis/>
      </LineChart>
      </Card> 


      <h2 id="stats-heading" data-cy="StatsHeading">
        Stats
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/stats/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Stats
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {statsList && statsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Distance</th>
                <th>Time</th>
                <th>Avgpace</th>
                <th>User</th>
                <th>Ranking</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {statsList.map((stats, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/stats/${stats.id}`} color="link" size="sm">
                      {stats.id}
                    </Button>
                  </td>
                  <td>{stats.distance}</td>
                  <td>{stats.time}</td>
                  <td>{stats.avgpace}</td>
                  <td>{stats.user ? stats.user.login : ''}</td>
                  <td>{stats.ranking ? <Link to={`/ranking/${stats.ranking.id}`}>{stats.ranking.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stats/${stats.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/stats/${stats.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/stats/${stats.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Stats found</div>
        )}
      </div>
    </div>
  );
};

export default Stats;
