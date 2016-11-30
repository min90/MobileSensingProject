package mobilesystems.mobilesensing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.R;
import mobilesystems.mobilesensing.models.Challenge;

/**
 * Created by Jesper on 30/11/2016.
 */

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengesViewHolder> {
    private static final String DEBUG_TAG = ChallengesAdapter.class.getSimpleName();
    private List<Challenge> challengeList;

    public ChallengesAdapter(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    public void swap(ArrayList<Challenge> challenges){
        challengeList.clear();
        challengeList.addAll(challenges);
        notifyDataSetChanged();
    }

    @Override
    public ChallengesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View challengeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenges, parent, false);
        return new ChallengesViewHolder(challengeView);
    }

    @Override
    public void onBindViewHolder(ChallengesViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);
        holder.txtChallengeTitle.setText(challenge.getChallengeTitle());
        holder.txtChallengeDescription.setText(challenge.getChallengeDescription());
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public class ChallengesViewHolder extends RecyclerView.ViewHolder {

        TextView txtChallengeTitle;
        TextView txtChallengeDescription;
        public ChallengesViewHolder(View itemView) {
            super(itemView);
            txtChallengeTitle = (TextView) itemView.findViewById(R.id.txtChallengeTitle);
            txtChallengeDescription = (TextView) itemView.findViewById(R.id.txtChallengeDescription);
        }
    }
}
