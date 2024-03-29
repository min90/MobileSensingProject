package mobilesystems.mobilesensing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.other.EnDecodeImages;

/**
 * Created by Jesper on 21/10/2016.
 */

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.TaskViewHolder> {
    private List<Issue> tasks;

    public ExploreAdapter(List<Issue> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_card, parent, false);
        return new TaskViewHolder(taskView);
    }

    public void swap(ArrayList<Issue> issues){
        tasks.clear();
        tasks.addAll(issues);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Issue task = tasks.get(position);
        holder.txtSubject.setText(task.getCategory());
        holder.txtSubjectDistance.setText(task.getSubjectDistance());
        holder.txtDistance.setText(String.valueOf(task.getDistance()).concat(" m"));
        holder.txtDescription.setText(task.getDescription());
        //TODO place Imageview with user pictures and according to subject
        holder.imgUserPicture.setImageBitmap(EnDecodeImages.decodeBase64(task.getPicture()));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubject, txtSubjectDistance, txtDistance, txtDescription;
        ImageView imgIcon, imgUserPicture;

        public TaskViewHolder(View itemView) {
            super(itemView);
            txtSubject = (TextView) itemView.findViewById(R.id.txt_subject);
            txtSubjectDistance = (TextView) itemView.findViewById(R.id.txt_subject_distance);
            txtDistance = (TextView) itemView.findViewById(R.id.txt_distance);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon_subject);
            imgUserPicture = (ImageView) itemView.findViewById(R.id.img_user_picture);
        }
    }
}
