package mobilesystems.mobilesensing.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cocoahero.android.geojson.GeoJSONObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.geofence.GeoJsonFetcher;
import mobilesystems.mobilesensing.json.GeoJSONParser;
import mobilesystems.mobilesensing.models.Task;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, com.google.android.gms.location.LocationListener {
    private final static String DEBUG_TAG = ExploreFragment.class.getSimpleName();

    private final static int MY_LOCATION_REQUEST_CODE = 00;
    public final static double ODENSE_LAT = 55.403756;
    public final static double ODENSE_LNG = 10.402370;

    private GoogleMap mGoogleMap;
    private LatLng odensePosition = new LatLng(ODENSE_LAT, ODENSE_LNG);
    private MapView mapView;
    private GoogleApiClient mGoogleApiClient;
    private Location lastKnownLocation;
    private LocationRequest mLocationRequest;
    private GeoJsonFetcher geoJsonFetcher;
    private GeoJSONParser geoJSONParser;
    private ArrayList<LatLng> latLngs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        generateTask();
        View view = inflater.inflate(R.layout.explore_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        geoJsonFetcher = new GeoJsonFetcher(getActivity());
        geoJSONParser = new GeoJSONParser();
        connectToGoogleAPI();
        createLocationRequest();
        setHasOptionsMenu(true);
        setToolbarTitle();
        return view;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void generateTask() {
        Task task = new Task();
        task.setDescription("Affald på hjørnet");
        task.setSubject("Affald");
        task.setLatitude(55.422138);
        task.setLongitude(10.254772);

        task.save();

        Task task1 = new Task();
        task1.setDescription("Smadret lampe");
        task1.setSubject("Belysning");
        task1.setLatitude(55.420044);
        task1.setLongitude(10.271659);

        task1.save();

        Task task2 = new Task();
        task2.setDescription("Smadret lampe");
        task2.setSubject("Belysning");
        task2.setLatitude(55.391272);
        task2.setLongitude(10.438900);

        task2.save();


    }

    private void connectToGoogleAPI() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setToolbarTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Udforsk kort");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.explorer_map_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explorer_list:
                FragmentTransactioner.get().transactFragments(getActivity(), new ExploreFragment(), "list_fragment");
                Toast.makeText(getActivity(), "Opening list....", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.explorer_map_search:
                //TODO search for a challenge
                //Toast.makeText(getActivity(), "Searching....", Toast.LENGTH_SHORT).show();
                //TODO just for testing
                return true;
            case R.id.explorer_update_markers:
                updateMarkers();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Log.d(DEBUG_TAG, "Ready");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            moveCamera(googleMap, 13, odensePosition);
            Toast.makeText(getActivity(), "Placerings tjeneste er ikke aktiv!", Toast.LENGTH_LONG).show();
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(this);

        if (lastKnownLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
        }

        updateMarkers();
        setUpBoundariesForOdense();
    }

    private void updateMarkers() {
        stopSearchingForMarkers();
        if (mGoogleMap != null) {
            for (MarkerOptions markerOptions : addMarkersByUsersPosition()) {
                mGoogleMap.addMarker(markerOptions);
            }
        }
    }

    private void moveCamera(GoogleMap mGoogleMap, int zoomLevel, LatLng latLng) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(makeInitialZoom(latLng, zoomLevel)));
            return;
        }
        Toast.makeText(getActivity(), "Kortet fejlede, prøv igen!", Toast.LENGTH_SHORT).show();
    }

    private CameraPosition makeInitialZoom(LatLng latLng, int zoomLevel) {
        return new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build();
    }

    private List<MarkerOptions> addMarkersByUsersPosition() {
        List<MarkerOptions> availableTasks = new ArrayList<>();
        List<Task> allTasks = Task.listAll(Task.class);

        double latitude;
        double longitude;

        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        } else {
            latitude = ODENSE_LAT;
            longitude = ODENSE_LNG;
        }

        for (Task task : allTasks) {
            if (distFrom(latitude, longitude, task.getLatitude(), task.getLongitude()) < 50) {
                availableTasks.add(generateMarker(task));
            }
        }

        return availableTasks;
    }

    private void setUpBoundariesForOdense() {
        try {
            if (latLngs == null || latLngs.isEmpty()) {
                GeoJSONObject geoJSONObject = geoJsonFetcher.readOdenseBoundaries();
                latLngs = geoJSONParser.parseOdenseBoundaries(geoJSONObject);
            }
        } catch (JSONException ex) {
            Log.e(DEBUG_TAG, "Unable to read GeoJSON", ex);
        }
    }

    private MarkerOptions generateMarker(Task task) {
        return new MarkerOptions()
                .position(new LatLng(task.getLatitude(), task.getLongitude()))
                .title(task.getSubject())
                .snippet(task.getDescription());
    }

    private boolean stopSearchingForMarkers() {
        if (lastKnownLocation != null && latLngs != null) {
            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            if (PolyUtil.containsLocation(latLng, latLngs, false)) {
                return true;
            } else {
                stopLocationUpdates();
                return false;
            }
        }
        return false;
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
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null) mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            moveCamera(mGoogleMap, 13, odensePosition);
            Toast.makeText(getActivity(), "Placerings tjeneste er ikke aktiv!", Toast.LENGTH_LONG).show();
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastKnownLocation != null) {
            Log.d(DEBUG_TAG, "Latitude: " + lastKnownLocation.getLatitude());
            Log.d(DEBUG_TAG, "Longitude: " + lastKnownLocation.getLongitude());
            moveCamera(mGoogleMap, 15, new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        }

        if (mLocationRequest != null && mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(DEBUG_TAG, "Failed to connect to google API " + connectionResult.getErrorMessage());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //TODO open info for task, find and marker with task.
//        Fragment taskInfoFragment = TaskInfoFragment.newInstance(null);
//        FragmentTransactioner.get().transactFragments(getActivity(), taskInfoFragment, "task_info_fragment");
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        if (stopSearchingForMarkers()) {
            updateMarkers();
        }
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }
}
