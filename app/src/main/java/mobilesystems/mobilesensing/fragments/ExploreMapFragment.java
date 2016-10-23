package mobilesystems.mobilesensing.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import mobilesystems.mobilesensing.MainActivity;
import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreMapFragment extends Fragment implements OnMapReadyCallback {
    private final static String DEBUG_TAG = ExploreFragment.class.getSimpleName();
    private final static int MY_LOCATION_REQUEST_CODE = 00;
    private GoogleMap mGoogleMap;
    private View view;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        setHasOptionsMenu(true);
        setToolbarTitle();
        return view;
    }

    private void setToolbarTitle() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Udforsk kort");
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
                Toast.makeText(getActivity(), "Searching....", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            setUpMap(googleMap);
        } else {
            // Show rationale and request permission.
            mGoogleMap = googleMap;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleMap != null) {
                    setUpMap(mGoogleMap);
                }
            } else {
                // Permission was denied. Display an error message.
                if (view != null) {
                    Snackbar.make(view, "Lokation er nødvendig!", Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Lokation er nødvendig!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setUpMap(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            }
        });
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
}
