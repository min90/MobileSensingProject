package mobilesystems.mobilesensing.models;

import android.graphics.Bitmap;

import mobilesystems.sugarorm.SugarRecord;


/**
 * Created by Jesper on 21/10/2016.
 */

public class Task extends SugarRecord {
    private int distance;
    private String subjectDistance;
    private String subject;
    private String description;
    private Bitmap subjectIcon;
    private Bitmap userPicture;

    public Task(int distance, String subjectDistance, String subject, String description) {
        this.distance = distance;
        this.subjectDistance = subjectDistance;
        this.subject = subject;
        this.description = description;
    }

    public Task(int distance, String subjectDistance, String subject, String description, Bitmap subjectIcon, Bitmap userPicture) {
        this.distance = distance;
        this.subjectDistance = subjectDistance;
        this.subject = subject;
        this.description = description;
        this.subjectIcon = subjectIcon;
        this.userPicture = userPicture;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getSubjectDistance() {
        return subjectDistance;
    }

    public void setSubjectDistance(String subjectDistance) {
        this.subjectDistance = subjectDistance;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getSubjectIcon() {
        return subjectIcon;
    }

    public void setSubjectIcon(Bitmap subjectIcon) {
        this.subjectIcon = subjectIcon;
    }

    public Bitmap getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(Bitmap userPicture) {
        this.userPicture = userPicture;
    }

    @Override
    public String toString() {
        return "Task{" +
                "distance=" + distance +
                ", subjectDistance='" + subjectDistance + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", subjectIcon=" + subjectIcon +
                ", userPicture=" + userPicture +
                '}';
    }
}
