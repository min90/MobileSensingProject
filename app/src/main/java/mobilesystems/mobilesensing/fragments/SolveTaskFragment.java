package mobilesystems.mobilesensing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.MainActivity;
import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.other.EnDecodeImages;
import mobilesystems.mobilesensing.persistence.FragmentTransactioner;
import mobilesystems.mobilesensing.persistence.NetworkPackager;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jesper on 05/11/2016.
 */

public class SolveTaskFragment extends Fragment implements View.OnClickListener {
    private final static String TASK_TAG = "task";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String DEBUG_TAG = SolveTaskFragment.class.getSimpleName();

    private ImageView imgTakenPhoto;
    private ImageButton imgTakePhoto;
    private EditText edtNoteDescription;
    private String encodedImageString;
    private Issue issue;
    private NetworkPackager networkPackager;

    public static SolveTaskFragment newInstance(Issue task) {
        SolveTaskFragment fragment = new SolveTaskFragment();

        Bundle args = new Bundle();
        args.putSerializable(TASK_TAG, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.solve_task, container, false);
        imgTakenPhoto = (ImageView) view.findViewById(R.id.imgTakenPhoto);
        imgTakePhoto = (ImageButton) view.findViewById(R.id.imgBtnTakePhoto);
        imgTakePhoto.setOnClickListener(this);
        edtNoteDescription = (EditText) view.findViewById(R.id.edtComment);

        Bundle bundle = getArguments();

        issue = (Issue) bundle.getSerializable(TASK_TAG);
        if (issue != null) {
        }
        setHasOptionsMenu(true);
        networkPackager = new NetworkPackager(getActivity());
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
                uploadProofoFTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean uploadProofoFTask() {
        List<Object> userPicture = new ArrayList<>();
        if (encodedImageString != null) {
            userPicture.add(encodedImageString);
        }
        //TODO upload proof
        if (issue != null && networkPackager != null) {
            if (!userPicture.isEmpty()) issue.setPictures(userPicture);
            //issue.setSolvedByUser(Integer.parseInt(MainActivity.getUser().getUserID()));
            issue.setSolvedByUser(123);
            networkPackager.updateIssue(issue);
        } else {
            Log.e(DEBUG_TAG, "Unable to upload issue update.");
        }
        goBackToMainScreen();
        return false;
    }

    private void goBackToMainScreen() {
        Toast.makeText(getActivity(), "Godt gået du har løst en opgave! - Opgaven valideres....", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgTakenPhoto.setImageBitmap(imageBitmap);
            encodeImage(imageBitmap);
        }
    }

    private void encodeImage(Bitmap imageBitmap) {
        encodedImageString = EnDecodeImages.encodeToBase64(imageBitmap, Bitmap.CompressFormat.PNG, 100);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == imgTakePhoto.getId()) {
            dispatchTakePictureIntent();
        }
    }
}
