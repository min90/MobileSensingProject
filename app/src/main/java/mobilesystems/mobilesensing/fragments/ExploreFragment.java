package mobilesystems.mobilesensing.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.adapters.ExploreAdapter;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.other.Util;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = ExploreFragment.class.getSimpleName();
    private final static String SHARED_TAG = "shared";
    public final static double ODENSE_LAT = 55.403756;
    public final static double ODENSE_LNG = 10.402370;

    private ExploreAdapter exploreAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private float longitude;
    private float latitude;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_list, container, false);
        setUpRecyclerView(view);
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_issues);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_TAG, Context.MODE_PRIVATE);
        fetchLastKnownLocation();
        return view;
    }

    private void setUpRecyclerView(View view) {
        exploreAdapter = new ExploreAdapter(fetchIssues());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.explore_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exploreAdapter);
    }

    private void fetchLastKnownLocation() {
        latitude = sharedPreferences.getFloat("Lat", (float) ODENSE_LAT);
        longitude = sharedPreferences.getFloat("Lng", (float) ODENSE_LNG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.explorer_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explorer_map:
                //TODO Open map
                FragmentTransactioner.get().transactFragments(getActivity(), new ExploreMapFragment(), "map_fragment");
                Toast.makeText(getActivity(), "Opening map....", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.explorer_search:
                //TODO search for a challenge
                Toast.makeText(getActivity(), "Searching....", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Issue> fetchIssues() {
        //TODO fetch tasks from API.
        ArrayList<Issue> providedIssues = new ArrayList<>();
        List<Issue> issues = Issue.listAll(Issue.class);
        for (Issue issue : issues) {
            if (Util.getInstance().distFrom(latitude, longitude, issue.getLatitude(), issue.getLongitude()) < 10) {
                providedIssues.add(issue);
            }
        }
        return providedIssues;
    }

    private void updateList() {
        //TODO update list after fetch.
        exploreAdapter.swap(fetchIssues());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateList();
    }
}
