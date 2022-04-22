export const loadMapApi = () => {

  // const MY_KEY = process.env.REACT_APP_API_KEY;
    const mapsURL = "https://maps.googleapis.com/maps/api/js?key=&libraries=places&language=no®ion=NO&v=3.48";

    const scripts = document.getElementsByTagName('script');

    for (let i = 0; i < scripts.length; i++) {
        if (scripts[i].src.indexOf(mapsURL) === 0) {
            return scripts[i];
        }
    }


const googleMapScript = document.createElement('script');
googleMapScript.src = mapsURL;
googleMapScript.async = true;
googleMapScript.defer = true;
window.document.body.appendChild(googleMapScript);

return googleMapScript;
}
