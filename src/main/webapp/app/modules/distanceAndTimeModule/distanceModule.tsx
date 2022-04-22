
// Haversine formula to get distance 

export function getDistance(lat, lng, endLat, endLng) {

    // distance between latitude and longitude
    const dLat = (endLat - lat) * Math.PI / 180.0;
    const dLng = (endLng - lng) * Math.PI / 180.0;

    // convert to radians
    lat = (lat) * Math.PI / 180.0;
    endLat = (endLat) * Math.PI / 180.0;
    
    // apply Haversine 
    const a = Math.pow(Math.sin(dLat / 2), 2) +
        Math.pow(Math.sin(dLng / 2), 2) *
        Math.cos(lat) * Math.cos(dLat);
    
    const rad = 6371;
    const c = 2 * Math.asin(Math.sqrt(a));
    return rad * c;
}