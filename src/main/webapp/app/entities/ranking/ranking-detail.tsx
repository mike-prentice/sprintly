import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ranking.reducer';

export const RankingDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const rankingEntity = useAppSelector(state => state.ranking.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rankingDetailsHeading">Ranking</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{rankingEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{rankingEntity.name}</dd>
          <dt>
            <span id="avgpace">Avgpace</span>
          </dt>
          <dd>{rankingEntity.avgpace}</dd>
          <dt>
            <span id="rank">Rank</span>
          </dt>
          <dd>{rankingEntity.rank}</dd>
          <dt>User</dt>
          <dd>{rankingEntity.user ? rankingEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/ranking" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ranking/${rankingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RankingDetail;
