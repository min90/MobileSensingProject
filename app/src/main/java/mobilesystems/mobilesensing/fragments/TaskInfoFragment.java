package mobilesystems.mobilesensing.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobilesystems.mobilesensing.models.Task;

/**
 * Created by Jesper on 05/11/2016.
 */

public class TaskInfoFragment extends Fragment {

    private final static String TASK_TAG = "task";

    public static TaskInfoFragment newInstance(Task task) {
        TaskInfoFragment fragment = new TaskInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable(TASK_TAG, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        Task task = (Task) bundle.getSerializable(TASK_TAG);
        if (task != null) {
            setUpInfo(task);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setUpInfo(Task task) {

    }
}
