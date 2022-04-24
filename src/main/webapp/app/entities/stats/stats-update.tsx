import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IRanking } from 'app/shared/model/ranking.model';
import { getEntities as getRankings } from 'app/entities/ranking/ranking.reducer';
import { IStats } from 'app/shared/model/stats.model';
import { getEntity, updateEntity, createEntity, reset } from './stats.reducer';

export const StatsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const rankings = useAppSelector(state => state.ranking.entities);
  const statsEntity = useAppSelector(state => state.stats.entity);
  const loading = useAppSelector(state => state.stats.loading);
  const updating = useAppSelector(state => state.stats.updating);
  const updateSuccess = useAppSelector(state => state.stats.updateSuccess);
  const handleClose = () => {
    props.history.push('/stats');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getRankings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...statsEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      ranking: rankings.find(it => it.id.toString() === values.ranking.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...statsEntity,
          user: statsEntity?.user?.id,
          ranking: statsEntity?.ranking?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sprintlyApp.stats.home.createOrEditLabel" data-cy="StatsCreateUpdateHeading">
            Create or edit a Stats
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="stats-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Distance" id="stats-distance" name="distance" data-cy="distance" type="text" />
              <ValidatedField label="Time" id="stats-time" name="time" data-cy="time" type="text" />
              <ValidatedField label="Avgpace" id="stats-avgpace" name="avgpace" data-cy="avgpace" type="text" />
              <ValidatedField id="stats-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="stats-ranking" name="ranking" data-cy="ranking" label="Ranking" type="select">
                <option value="" key="0" />
                {rankings
                  ? rankings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stats" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StatsUpdate;
