package mobilesystems.mobilesensing.geofence;

import android.content.Context;
import android.util.Log;

import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;
import com.google.android.gms.location.Geofence;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import mobilesystems.mobilesensing.fragments.ExploreMapFragment;

/**
 * Created by Jesper on 06/11/2016.
 */

public class GeoFencingProvider {

    private static final String DEBUG_TAG = GeoFencingProvider.class.getSimpleName();

    private Context context;

    private static final String GEOFENCE_REQ_ID = "00";
    private static final float RADIUS = 100;
    private static final long DURATION = 100;

    public GeoFencingProvider(Context context) {
        this.context = context;
    }

//    public void iniateGeofencing() {
//        Geofence geofence = new Geofence.Builder()
//                .setRequestId(GEOFENCE_REQ_ID) // Geofence ID
//                .setCircularRegion(ExploreMapFragment.ODENSE_LAT, ExploreMapFragment.ODENSE_LNG, RADIUS) // defining fence region
//                .setExpirationDuration( DURATION ) // expiring date
//                // Transition types that it should look for
//                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
//                .build();
//    }

    public GeoJSONObject readOdenseBoundaries() throws JSONException {
        String odenseBounds = "odense_bounds.geojson";
        String parsedGeoJson = "";
        try {
            InputStream is = context.getAssets().open(odenseBounds);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            parsedGeoJson = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(DEBUG_TAG, "Unable to read boundaries", ex);
        }
        return GeoJSON.parse(parsedGeoJson);
    }
}
