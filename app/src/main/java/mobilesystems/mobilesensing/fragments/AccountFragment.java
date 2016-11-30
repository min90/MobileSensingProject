package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import mobilesystems.mobilesensing.R;

/**
 * Created by Jesper on 21/10/2016.
 */

public class AccountFragment extends Fragment {
    private ListView solvedListview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account, container, false);

        solvedListview = (ListView) view.findViewById(R.id.solved_list_view);
        setUpListview();
        return view;
    }

    private void setUpListview() {
        String[] solvedIssues = getActivity().getResources().getStringArray(R.array.solved_issue);
        ArrayAdapter solvedAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, solvedIssues);
        solvedListview.setAdapter(solvedAdapter);
    }
}
