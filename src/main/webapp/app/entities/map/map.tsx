import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMap } from 'app/shared/model/map.model';
import { getEntities } from './map.reducer';
import Gmap from 'app/modules/home/Gmap';
import { loadMapApi } from 'app/modules/home/GoogleMapsUtils';
import 'app/modules/home/Map.scss';

export const Map = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const mapList = useAppSelector(state => state.map.entities);
  const loading = useAppSelector(state => state.map.loading);
  const [scriptLoaded, setScriptLoaded] = useState(false);
  const [lat, setLat] = useState(null);
  const [lng, setLng] = useState(null);
  const [timeStart, setTimeStart] = useState(null);
  const [status, setStatus] = useState(null);
  const [endLat, setEndLat] = useState(null);
  const [endLng, setEndLng] = useState(null);
  const [timeEnd, setTimeEnd] = useState(null);
  let watchID;

  const startWatching = () => {
      setStatus('Locating...');
  watchID = navigator.geolocation.watchPosition((position) => {
    setStatus(null);
    setLat(position.coords.latitude);
    setLng(position.coords.longitude);
    setTimeStart(position.timestamp);
    
  }, () => {
    setStatus('Unable to retrieve location');
  });
  }
  
  const stopWatching = () => {
    navigator.geolocation.clearWatch(watchID);
    navigator.geolocation.getCurrentPosition((positionEnd) => {
      setEndLat(positionEnd.coords.latitude);
      setEndLng(positionEnd.coords.longitude);
      setTimeEnd(positionEnd.timestamp);
    }, () => {
      setStatus('Great Run!');
    });
  } 
  const runStart = new Date(timeStart * 1000);
  

  useEffect(() => {
    const googleMapScript = loadMapApi();
    googleMapScript.addEventListener('load', function () {
      setScriptLoaded(true);
    })
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div className="container-fluid">
       <div className="map-container">
              {scriptLoaded && (
                <Gmap mapType={google.maps.MapTypeId.ROADMAP} mapTypeControl={true}/>
              )}
      </div>
      <div className="d-flex justify-content-center">
        <button className="btn btn-primary justify-content-center" onClick={startWatching}>Start Run</button>
        <button className="btn btn-primary justify-content-center" onClick={stopWatching}>Stop Run</button>
        </div>
      <p>{status}</p>
      {lat && <p>Latitude: {lat}</p>}
      {lng && <p>Longitude: {lng}</p>}
      {timeStart && <p>Time Stamp {timeStart}</p>}
      <p>{status}</p>
      {endLat && <p>Latitude: {endLat}</p>}
      {endLng && <p>Longitude: {endLng}</p>}
      {timeEnd && <p>Time Stamp {timeEnd}</p>}
      
      
      <h2 id="map-heading" data-cy="MapHeading">
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/map/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Map
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mapList && mapList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Distance</th>
                <th>Time Start</th>
                <th>Time Stop</th>
                <th>Total Time</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mapList.map((map, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/map/${map.id}`} color="link" size="sm">
                      {map.id}
                    </Button>
                  </td>
                  <td>{map.distance}</td>
                  <td>{map.timeStart ? <TextFormat type="date" value={map.timeStart} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{map.timeStop ? <TextFormat type="date" value={map.timeStop} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{map.stats ? <Link to={`/stats/${map.stats.id}`}>{map.stats.distance}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/map/${map.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/map/${map.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/map/${map.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Runs found</div>
        )}
      </div>
    </div>
  );
};

export default Map;
