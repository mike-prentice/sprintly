import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './map.reducer';

export const MapDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const mapEntity = useAppSelector(state => state.map.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mapDetailsHeading">Map</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{mapEntity.id}</dd>
          <dt>
            <span id="distance">Distance</span>
          </dt>
          <dd>{mapEntity.distance}</dd>
          <dt>
            <span id="timeStart">Time Start</span>
          </dt>
          <dd>{mapEntity.timeStart ? <TextFormat value={mapEntity.timeStart} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="timeStop">Time Stop</span>
          </dt>
          <dd>{mapEntity.timeStop ? <TextFormat value={mapEntity.timeStop} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Stats</dt>
          <dd>{mapEntity.stats ? mapEntity.stats.distance : ''}</dd>
        </dl>
        <Button tag={Link} to="/map" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/map/${mapEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MapDetail;
