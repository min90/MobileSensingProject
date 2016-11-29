package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Challenge;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ChallengesFragment extends Fragment {

    private TextView txtTitle;
    private TextView txtDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges, container, false);

        txtTitle = (TextView) view.findViewById(R.id.txtChallengeTitle);
        txtDescription = (TextView) view.findViewById(R.id.txtChallengeDescription);

        return view;
    }

    @SuppressWarnings("unchecked")
    private List<Challenge> getChallenges() {
        List<Challenge> challenges = (List<Challenge>) Challenge.findAll(Challenge.class);
        return challenges;
    }
}
