package mobilesystems.mobilesensing.other;

import android.util.Log;

/**
 * Created by Jesper on 29/11/2016.
 */
public class Util {
    private static final String DEBUG_TAG = Util.class.getSimpleName();
    private static Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }

    /**
     * Implementation of the Haversine formula to calculate distance between two points
     *
     * @param lat1 latitude of user
     * @param lng1 longitude of user
     * @param lat2 latitude of task
     * @param lng2 longitude of task
     * @return the distance
     */
    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        Log.d(DEBUG_TAG, "Afstand: " + dist);
        return dist;
    }
}
