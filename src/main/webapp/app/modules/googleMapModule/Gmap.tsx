import React, { useEffect, useRef, useState } from 'react';
import 'app/modules/googleMapModule/Map.scss';

interface IGmap { 
    mapType: google.maps.MapTypeId, 
    mapTypeControl?: boolean;
}

type GoogleLatLng = google.maps.LatLng;
type GoogleMap = google.maps.Map;



const Map: React.FC<IGmap> = ({ mapType, mapTypeControl = false }) => {
    const ref = useRef<HTMLDivElement>(null);
    const [map, setMap] = useState<GoogleMap>();

    const startMap = (): void => {
        if (!map) { defaultMapStart();
    }
};
useEffect(startMap, [map]);


    const defaultMapStart = (): void => {
        const defaultAddress = new google.maps.LatLng(39.746, -75.549);
        initMap(18, defaultAddress);
    }

    const initMap = (zoomlevel: number, address: GoogleLatLng): void => {
        if (ref.current) {
            setMap(
                new google.maps.Map(ref.current, {
                    zoom: zoomlevel,
                    center: address,
                    mapTypeControl,
                    streetViewControl: false,
                    zoomControl: true,
                    mapTypeId: mapType
                })
            )
        }
    };
    
   
    return (
        <div className="map-container">
            <div ref={ref} className="map-container"></div>
        </div>
    );
}
export default Map;