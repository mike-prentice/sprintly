
// Haversine formula to get distance 

export function getDistance(lat, lng, endLat, endLng) {
    const R = 6371;
    const dLat = (endLat - lat) *(Math.PI/180);
    const dLong = (endLng - lng) *(Math.PI/180);
    
    const a = Math.sin(dLat / 2)
            *
            Math.sin(dLat / 2) 
            +
            Math.cos(lat * (Math.PI /180))
            * 
            Math.cos(endLat * (Math.PI /180)) 
            *
            Math.sin(dLong / 2) 
            *
            Math.sin(dLong / 2);

    const c = 2 * Math.asin(Math.sqrt(a));
    const distance = R * c;

    return distance;
}

