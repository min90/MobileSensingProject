package mobilesystems.mobilesensing.models;

import com.google.gson.annotations.SerializedName;

import mobilesystems.sugarorm.SugarRecord;

/**
 * Created by Jesper on 15/11/2016.
 */

public class Challenge extends SugarRecord {


    private int challengeId;
    @SerializedName("title")
    private String challengeTitle;
    @SerializedName("description")
    private String challengeDescription;

    public Challenge() {
    }

    public Challenge(String challengeTitle, String challengeDescription) {
        this.challengeId = challengeId;
        this.challengeTitle = challengeTitle;
        this.challengeDescription = challengeDescription;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "challengeId=" + challengeId +
                ", challengeTitle='" + challengeTitle + '\'' +
                ", challengeDescription='" + challengeDescription + '\'' +
                '}';
    }
}
