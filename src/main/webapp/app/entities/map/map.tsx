import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMap } from 'app/shared/model/map.model';
import { getEntities } from './map.reducer';
import Gmap from 'app/modules/googleMapModule/Gmap';
import { loadMapApi } from 'app/modules/googleMapModule/GoogleMapsUtils';
import 'app/modules/googleMapModule/Map.scss';
import { getDistance } from 'app/modules/distanceAndTimeModule/distanceModule';
import { getDuration } from 'app/modules/distanceAndTimeModule/timeModule';

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
  const [distance, setDistance] = useState(null);
  const [duration, setDuration] = useState(null);
  let distances; 
  let watchID;

  const startWatching = () => {
    setStatus('Locating...');
    watchID = navigator.geolocation.watchPosition(
      position => {
        setStatus(null);
        setLat(position.coords.latitude);
        setLng(position.coords.longitude);
        setTimeStart(position.timestamp);
        // eslint-disable-next-line no-console
        console.log(position);
      },
      () => {
        setStatus('Unable to retrieve location');
      }
    );
  };

  const endPosition = () => {
    setStatus('Nice Run!');
    navigator.geolocation.getCurrentPosition(
      position => {
        setEndLat(position.coords.latitude);
        setEndLng(position.coords.longitude);
        setTimeEnd(position.timestamp);
        // eslint-disable-next-line no-console
        console.log(position);
      },
      () => {
        setStatus('Unable to retrieve final location');
      }
    );
  };

  const getDurationFunction = () => {
     setDuration(getDuration(timeStart, timeEnd));
    
    // eslint-disable-next-line no-console
    console.log(duration);
  };

  const getDistanceFunction = () => {
     distances = google.maps.geometry.spherical.computeDistanceBetween(new google.maps.LatLng(lat, lng), new google.maps.LatLng(endLat, endLng));
    // setDistance(getDistance(lat, lng, endLat, endLng));
     // eslint-disable-next-line no-console
    console.log(distances);
  };

  const stopWatching = () => {
    navigator.geolocation.clearWatch(watchID);
    endPosition();
  };

  useEffect(() => {
    const googleMapScript = loadMapApi();
    googleMapScript.addEventListener('load', function () {
      setScriptLoaded(true);
    });
    dispatch(getEntities({}));
    
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div className="container-fluid">
      <div className="map-container">{scriptLoaded && <Gmap mapType={google.maps.MapTypeId.ROADMAP} mapTypeControl={true} />}</div>
      <div className="d-flex justify-content-center">
        <button className="btn btn-primary justify-content-center" onClick={startWatching}>
          Start Run
        </button>
        <button className="btn btn-primary justify-content-center" onClick={stopWatching}>
          Stop Run
        </button>
        <button className="btn btn-primary justify-content-center" onClick={getDistanceFunction}>
          Get Distance
        </button>
        <button className="btn btn-primary justify-content-center" onClick={getDurationFunction}>
          Get Duration
        </button>
      </div>
      <p>{status}</p>
      {lat && <p>Latitude: {lat}</p>}
      {lng && <p>Longitude: {lng}</p>}
      {timeStart && <p>Time Stamp {timeStart}</p>}

      {endLat && <p>End Latitude: {endLat}</p>}
      {endLng && <p>End Longitude: {endLng}</p>}
      {timeEnd && <p> End Time Stamp {timeEnd}</p>}
      {<p>{distance}</p>}
      {<p>{duration}</p>}
    </div>
    //      <h2 id="map-heading" data-cy="MapHeading">
    //         Maps
    //         <div className="d-flex justify-content-end">
    //           <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
    //             <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
    //           </Button>
    //           <Link to="/map/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
    //             <FontAwesomeIcon icon="plus" />
    //             &nbsp; Create new Map
    //           </Link>
    //         </div>
    //       </h2>
    //       <div className="table-responsive">
    //         {mapList && mapList.length > 0 ? (
    //           <Table responsive>
    //             <thead>
    //               <tr>
    //                 <th>ID</th>
    //                 <th />
    //               </tr>
    //             </thead>
    //             <tbody>
    //               {mapList.map((map, i) => (
    //                 <tr key={`entity-${i}`} data-cy="entityTable">
    //                   <td>
    //                     <Button tag={Link} to={`/map/${map.id}`} color="link" size="sm">
    //                       {map.id}
    //                     </Button>
    //                   </td>
    //                   <td className="text-end">
    //                     <div className="btn-group flex-btn-group-container">
    //                       <Button tag={Link} to={`/map/${map.id}`} color="info" size="sm" data-cy="entityDetailsButton">
    //                         <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
    //                       </Button>
    //                       <Button tag={Link} to={`/map/${map.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
    //                         <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
    //                       </Button>
    //                       <Button tag={Link} to={`/map/${map.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
    //                         <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
    //                       </Button>
    //                     </div>
    //                   </td>
    //                 </tr>
    //               ))}
    //             </tbody>
    //           </Table>
    //         ) : (
    //           !loading && <div className="alert alert-warning">No Maps found</div>
    //         )}
    //       </div>
    //     </div>
  );
};

export default Map;
