import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trends.reducer';

export const TrendsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const trendsEntity = useAppSelector(state => state.trends.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="trendsDetailsHeading">Trends</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{trendsEntity.id}</dd>
          <dt>
            <span id="avgPace">Avg Pace</span>
          </dt>
          <dd>{trendsEntity.avgPace}</dd>
          <dt>
            <span id="distancePerRun">Distance Per Run</span>
          </dt>
          <dd>{trendsEntity.distancePerRun}</dd>
          <dt>User</dt>
          <dd>{trendsEntity.user ? trendsEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/trends" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trends/${trendsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TrendsDetail;
