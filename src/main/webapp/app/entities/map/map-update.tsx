import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStats } from 'app/shared/model/stats.model';
import { getEntities as getStats } from 'app/entities/stats/stats.reducer';
import { IMap } from 'app/shared/model/map.model';
import { getEntity, updateEntity, createEntity, reset } from './map.reducer';

export const MapUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const stats = useAppSelector(state => state.stats.entities);
  const mapEntity = useAppSelector(state => state.map.entity);
  const loading = useAppSelector(state => state.map.loading);
  const updating = useAppSelector(state => state.map.updating);
  const updateSuccess = useAppSelector(state => state.map.updateSuccess);
  const handleClose = () => {
    props.history.push('/map');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getStats({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timeStart = convertDateTimeToServer(values.timeStart);
    values.timeStop = convertDateTimeToServer(values.timeStop);

    const entity = {
      ...mapEntity,
      ...values,
      stats: stats.find(it => it.id.toString() === values.stats.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timeStart: displayDefaultDateTime(),
          timeStop: displayDefaultDateTime(),
        }
      : {
          ...mapEntity,
          timeStart: convertDateTimeFromServer(mapEntity.timeStart),
          timeStop: convertDateTimeFromServer(mapEntity.timeStop),
          stats: mapEntity?.stats?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sprintlyApp.map.home.createOrEditLabel" data-cy="MapCreateUpdateHeading">
            Create or edit a Map
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="map-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Distance" id="map-distance" name="distance" data-cy="distance" type="text" />
              <ValidatedField
                label="Time Start"
                id="map-timeStart"
                name="timeStart"
                data-cy="timeStart"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Time Stop"
                id="map-timeStop"
                name="timeStop"
                data-cy="timeStop"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="map-stats" name="stats" data-cy="stats" label="Stats" type="select">
                <option value="" key="0" />
                {stats
                  ? stats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.distance}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/map" replace color="info">
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

export default MapUpdate;
