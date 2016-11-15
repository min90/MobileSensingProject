package mobilesystems.mobilesensing.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import mobilesystems.sugarorm.SugarRecord;


/**
 * Created by Jesper on 21/10/2016.
 */

public class Task extends SugarRecord implements Serializable {
    @SerializedName("distance")
    private int distance;
    @SerializedName("subject_distance")
    private String subjectDistance;
    @SerializedName("subject")
    private String subject;
    @SerializedName("description")
    private String description;

    private Bitmap subjectIcon;
    private Bitmap userPicture;
    private double latitude;
    private double longitude;
    private int taskId;

    public Task() {
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", taskId=" + taskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (distance != task.distance) return false;
        if (Double.compare(task.latitude, latitude) != 0) return false;
        if (Double.compare(task.longitude, longitude) != 0) return false;
        if (taskId != task.taskId) return false;
        if (subjectDistance != null ? !subjectDistance.equals(task.subjectDistance) : task.subjectDistance != null)
            return false;
        if (subject != null ? !subject.equals(task.subject) : task.subject != null) return false;
        if (description != null ? !description.equals(task.description) : task.description != null)
            return false;
        if (subjectIcon != null ? !subjectIcon.equals(task.subjectIcon) : task.subjectIcon != null)
            return false;
        return userPicture != null ? userPicture.equals(task.userPicture) : task.userPicture == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = distance;
        result = 31 * result + (subjectDistance != null ? subjectDistance.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (subjectIcon != null ? subjectIcon.hashCode() : 0);
        result = 31 * result + (userPicture != null ? userPicture.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + taskId;
        return result;
    }
}
