package mobilesystems.mobilesensing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Task;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jesper on 05/11/2016.
 */

public class TaskInfoFragment extends Fragment implements View.OnClickListener {
    private final static String TASK_TAG = "task";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imgTakenPhoto;
    private ImageButton imgTakePhoto;
    private TextView txtTaskDescription;

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
        View view = inflater.inflate(R.layout.task_info, container, false);
        imgTakenPhoto = (ImageView) view.findViewById(R.id.imgTakenPhoto);
        imgTakePhoto = (ImageButton) view.findViewById(R.id.imgBtnTakePhoto);
        imgTakePhoto.setOnClickListener(this);
        txtTaskDescription = (TextView) view.findViewById(R.id.txtTaskDescription);

        Bundle bundle = getArguments();

        Task task = (Task) bundle.getSerializable(TASK_TAG);
        if (task != null) {
            setUpInfo(task);
        }
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_check_task:
                //TODO Send in the solved task with photos

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpInfo(Task task) {
        txtTaskDescription.setText(task.getDescription());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgTakenPhoto.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == imgTakePhoto.getId()) {
            dispatchTakePictureIntent();
        }
    }
}
