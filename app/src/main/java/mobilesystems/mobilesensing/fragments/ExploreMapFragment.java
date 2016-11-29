package mobilesystems.mobilesensing.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.geofence.GeoJsonFetcher;
import mobilesystems.mobilesensing.json.GeoJSONParser;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.other.Util;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;
import mobilesystems.mobilesensing.persistence.NetworkPackager;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, com.google.android.gms.location.LocationListener,
        GoogleMap.OnInfoWindowClickListener {
    private final static String DEBUG_TAG = ExploreFragment.class.getSimpleName();

    private final static int MY_LOCATION_REQUEST_CODE = 00;
    private final static String SHARED_TAG = "shared";
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
    private LinkedHashMap<Marker, Issue> markers;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        geoJsonFetcher = new GeoJsonFetcher(getActivity());
        geoJSONParser = new GeoJSONParser();
        markers = new LinkedHashMap<>();
        connectToGoogleAPI();
        createLocationRequest();
        setHasOptionsMenu(true);
        setToolbarTitle();
        NetworkPackager networkPackager = new NetworkPackager(getActivity());
        networkPackager.getIssues();
        networkPackager.getChallenges();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return view;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        googleMap.setOnInfoWindowClickListener(this);

        if (lastKnownLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
        }

        setUpBoundariesForOdense();
        updateMarkers();
    }

    private void updateMarkers() {
        stopSearchingForMarkers();
        if (mGoogleMap != null) {
            for (Map.Entry<Issue, MarkerOptions> markerOptionsEntry : addMarkersByUsersPosition().entrySet()) {
                Marker marker = mGoogleMap.addMarker(markerOptionsEntry.getValue());
                if (markers != null) {
                    markers.put(marker, markerOptionsEntry.getKey());
                }
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

    private LinkedHashMap<Issue, MarkerOptions> addMarkersByUsersPosition() {
        LinkedHashMap<Issue, MarkerOptions> availableTasks = new LinkedHashMap<>();
        List<Issue> allTasks = Issue.listAll(Issue.class);
        Log.d(DEBUG_TAG, "Tasks: " + allTasks);
        double latitude;
        double longitude;

        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        } else {
            latitude = ODENSE_LAT;
            longitude = ODENSE_LNG;
        }

        for (int i = 0; i < allTasks.size(); i++) {
            if (Util.getInstance().distFrom(latitude, longitude, allTasks.get(i).getLatitude(), allTasks.get(i).getLongitude()) < 10) {
                if (markers != null) {
                    MarkerOptions markerOptions = generateMarker(allTasks.get(i));
                    availableTasks.put(allTasks.get(i), markerOptions);
                }
            }
        }
        return availableTasks;
    }

    private MarkerOptions generateMarker(Issue task) {
        return new MarkerOptions()
                .position(new LatLng(task.getLatitude(), task.getLongitude()))
                .title(task.getCategory())
                .snippet(task.getDescription());
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


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        if(mapView != null) mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mapView != null) mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        if(mapView != null) mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        if(mapView != null) mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
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
            editor.putFloat("Lat", (float) lastKnownLocation.getLatitude()).commit();
            editor.putFloat("Lng", (float) lastKnownLocation.getLongitude()).commit();
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
        Log.d(DEBUG_TAG, "Clicked");
        Issue task = null;
        if (markers != null) {
            task = markers.get(marker);
        }
        Fragment taskInfoFragment = TaskInfoFragment.newInstance(task);
        FragmentTransactioner.get().transactFragments(getActivity(), taskInfoFragment, "task_info_fragment");
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(DEBUG_TAG, "Clicked");
    }
}
