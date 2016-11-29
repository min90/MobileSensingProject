package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.other.EnDecodeImages;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

/**
 * Created by Jesper on 29/11/2016.
 */

public class TaskInfoFragment extends Fragment implements View.OnClickListener {
    private final static String ISSUE_TAG = "issue";
    private final static String DEBUG_TAG = TaskInfoFragment.class.getSimpleName();

    private TextView txtInfoCategory;
    private TextView txtInfoSubject;
    private TextView txtDistance;
    private ImageView imgInfoUserPicture;
    private TextView txtInfoDescription;
    private Button btnSolveIssue;
    private Issue issue;

    public static TaskInfoFragment newInstance(Issue issue) {

        Bundle args = new Bundle();

        TaskInfoFragment fragment = new TaskInfoFragment();
        fragment.setArguments(args);
        args.putSerializable(ISSUE_TAG, issue);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_info, container, false);

        txtInfoCategory = (TextView) view.findViewById(R.id.txt_info_category);
        txtInfoSubject = (TextView) view.findViewById(R.id.txt_info_subject);
        txtDistance = (TextView) view.findViewById(R.id.txt_distance);
        txtInfoDescription = (TextView) view.findViewById(R.id.txt_info_description);
        imgInfoUserPicture = (ImageView) view.findViewById(R.id.img_info_user_picture);
        btnSolveIssue = (Button) view.findViewById(R.id.btn_solve_issue);
        btnSolveIssue.setOnClickListener(this);

        issue = (Issue) getArguments().getSerializable(ISSUE_TAG);
        if (issue != null) {
            setUpInfoDisplay(issue);
        }

        return view;
    }

    private void setUpInfoDisplay(Issue issue) {
        txtInfoCategory.setText(issue.getCategory());
        txtInfoSubject.setText(issue.getElement());
        txtInfoDescription.setText(issue.getDescription());
        txtDistance.setText("100 m"); //TODO skal have afstanden til denne.
        setUpInfoImage(issue);
    }

    private void setUpInfoImage(Issue issue) {
        try {
            Log.d(DEBUG_TAG, "Picture: " + issue.getPicture());
            String base64 = issue.getPicture();
            imgInfoUserPicture.setImageBitmap(EnDecodeImages.decodeBase64(base64));
        } catch (ClassCastException | NullPointerException ex) {
            Log.e(DEBUG_TAG, "Unable to cast picture base 64 to string", ex);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnSolveIssue.getId()) {
            if (issue != null) {
                Fragment taskInfoFragment = SolveTaskFragment.newInstance(issue);
                FragmentTransactioner.get().transactFragments(getActivity(), taskInfoFragment, "task_info_fragment");
            } else {
                Toast.makeText(getActivity(), "Desværre kunne vi ikke åbne opgaven, prøv igen!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
