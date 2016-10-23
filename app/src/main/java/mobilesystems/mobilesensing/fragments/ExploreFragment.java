package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import mobilesystems.mobilesensing.models.Task;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreFragment extends Fragment {
    private static final String DEBUG_TAG = ExploreFragment.class.getSimpleName();
    private ExploreAdapter exploreAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_list, container, false);
        setUpRecyclerView(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setUpRecyclerView(View view) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(testData());
        exploreAdapter = new ExploreAdapter(tasks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.explore_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(exploreAdapter);

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

    private Task testData() {
        String description = getString(R.string.lorem);
        return new Task(100, "in trash - 24m ago", "Full trash", description);
    }

    private void fetchTasks() {
        //TODO fetch tasks from API.
    }

    private void updateList() {
        //TODO update list after fetch.
    }
}
