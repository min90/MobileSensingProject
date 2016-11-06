package mobilesystems.mobilesensing.json;

import android.util.Log;

import com.cocoahero.android.geojson.GeoJSONObject;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jesper on 06/11/2016.
 */

public class GeoJSONParser {
    private static final String DEBUG_TAG = GeoJSONParser.class.getSimpleName();

    public GeoJSONParser() {
    }

    public ArrayList<LatLng> parseOdenseBoundaries(GeoJSONObject odenseBounds) {
        ArrayList<LatLng> boundsCoordinates = new ArrayList<>();
        try {
            JSONObject boundObject = odenseBounds.toJSON();
            JSONArray geometriesArray = boundObject.getJSONArray("geometries");
            JSONObject firstObject = geometriesArray.getJSONObject(0);
            JSONArray coordinates = firstObject.getJSONArray("coordinates");

            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray innerArray = coordinates.getJSONArray(i);
                for (int j = 0; j < innerArray.length(); j++) {
                    JSONArray innerInnerArray = innerArray.getJSONArray(j);
                    for (int k = 0; k < innerInnerArray.length(); k++) {
                        JSONArray latlngs = innerInnerArray.getJSONArray(k);
                        for (int l = 0; l < latlngs.length(); l++) {
                            LatLng latLng = new LatLng(Double.parseDouble(latlngs.getString(1)), Double.parseDouble(latlngs.getString(0)));
                            boundsCoordinates.add(latLng);
                            Log.d(DEBUG_TAG, "Longitude: " + latlngs.getString(0));
                            Log.d(DEBUG_TAG, "Latitude: " + latlngs.getString(1));
                        }
                    }
                }
            }

        } catch (JSONException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return boundsCoordinates;
    }

}
