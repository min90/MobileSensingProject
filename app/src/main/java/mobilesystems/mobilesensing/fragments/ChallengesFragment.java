package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.adapters.ChallengesAdapter;
import mobilesystems.mobilesensing.adapters.ExploreAdapter;
import mobilesystems.mobilesensing.models.Challenge;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ChallengesFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_list, container, false);

        setUpRecyclerView(view);
        return view;
    }

    private void setUpRecyclerView(View view) {
        ChallengesAdapter challengesAdapter = new ChallengesAdapter(getChallenges());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.challenge_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(challengesAdapter);
    }

    @SuppressWarnings("unchecked")
    private List<Challenge> getChallenges() {
        List<Challenge> challenges = (List<Challenge>) Challenge.findAll(Challenge.class);
        return challenges;
    }
}
