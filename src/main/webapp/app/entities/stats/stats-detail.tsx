import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stats.reducer';

export const StatsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const statsEntity = useAppSelector(state => state.stats.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="statsDetailsHeading">Stats</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{statsEntity.id}</dd>
          <dt>
            <span id="distanceRan">Distance Ran</span>
          </dt>
          <dd>{statsEntity.distanceRan}</dd>
          <dt>
            <span id="time">Time</span>
          </dt>
          <dd>
            {statsEntity.time ? <DurationFormat value={statsEntity.time} /> : null} ({statsEntity.time})
          </dd>
          <dt>
            <span id="cadence">Cadence</span>
          </dt>
          <dd>{statsEntity.cadence}</dd>
          <dt>
            <span id="avgpace">Avgpace</span>
          </dt>
          <dd>{statsEntity.avgpace}</dd>
          <dt>User</dt>
          <dd>{statsEntity.user ? statsEntity.user.login : ''}</dd>
          <dt>Ranking</dt>
          <dd>{statsEntity.ranking ? statsEntity.ranking.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/stats" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stats/${statsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StatsDetail;
