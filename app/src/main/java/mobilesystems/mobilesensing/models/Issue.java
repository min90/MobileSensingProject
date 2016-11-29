package mobilesystems.mobilesensing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mobilesystems.sugarorm.SugarRecord;


/**
 * Created by Jesper on 21/10/2016.
 */

public class Issue extends SugarRecord implements Serializable {
    private int distance;
    private String subjectDistance;

    @SerializedName("category")
    private String category;
    @SerializedName("element")
    private String element;
    @SerializedName("description")
    private String description;
    @SerializedName("comment")
    private String comment;
    @SerializedName("time")
    private Long time;


    @SerializedName("pictures")
    @Expose
    private List<Object> pictures = new ArrayList<>();

    @SerializedName("coordinates")
    @Expose
    private List<Object> latLng = new ArrayList<>();


    private double latitude;
    private double longitude;
    private String picture;

    @SerializedName("reward")
    private int reward;
    @SerializedName("created_by")
    private int createdByUser;
    @SerializedName("solved_by")
    private int solvedByUser;

    @SerializedName("objectId")
    private String issueId;

    private int taskId;

    public Issue() {
    }

    public Issue(int distance, String subjectDistance, String category, String description) {
        this.distance = distance;
        this.subjectDistance = subjectDistance;
        this.category = category;
        this.description = description;
    }

    public Issue(int distance, String subjectDistance, String category, String element,
                 String description, String comment, Long time, List<Object> pictures, List<Object> latLng,
                 double latitude, double longitude, int reward, int createdByUser, int solvedByUser, String issueId, int taskId) {
        this.distance = distance;
        this.subjectDistance = subjectDistance;
        this.category = category;
        this.element = element;
        this.description = description;
        this.comment = comment;
        this.time = time;
        this.pictures = pictures;
        this.latLng = latLng;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reward = reward;
        this.createdByUser = createdByUser;
        this.solvedByUser = solvedByUser;
        this.issueId = issueId;
        this.taskId = taskId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<Object> getPictures() {
        return pictures;
    }

    public void setPictures(List<Object> pictures) {
        this.pictures = pictures;
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

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(int createdByUser) {
        this.createdByUser = createdByUser;
    }

    public int getSolvedByUser() {
        return solvedByUser;
    }

    public void setSolvedByUser(int solvedByUser) {
        this.solvedByUser = solvedByUser;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public List<Object> getLatLng() {
        return latLng;
    }

    public void setLatLng(List<Object> latLng) {
        this.latLng = latLng;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "distance=" + distance +
                ", subjectDistance='" + subjectDistance + '\'' +
                ", category='" + category + '\'' +
                ", element='" + element + '\'' +
                ", description='" + description + '\'' +
                ", comment='" + comment + '\'' +
                ", time=" + time +
                ", pictures=" + pictures.isEmpty() +
                ", latLng=" + latLng +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", picture='" + picture + '\'' +
                ", reward=" + reward +
                ", createdByUser=" + createdByUser +
                ", solvedByUser=" + solvedByUser +
                ", issueId='" + issueId + '\'' +
                ", taskId=" + taskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (distance != issue.distance) return false;
        if (Double.compare(issue.latitude, latitude) != 0) return false;
        if (Double.compare(issue.longitude, longitude) != 0) return false;
        if (reward != issue.reward) return false;
        if (createdByUser != issue.createdByUser) return false;
        if (solvedByUser != issue.solvedByUser) return false;
        if (taskId != issue.taskId) return false;
        if (subjectDistance != null ? !subjectDistance.equals(issue.subjectDistance) : issue.subjectDistance != null)
            return false;
        if (category != null ? !category.equals(issue.category) : issue.category != null)
            return false;
        if (element != null ? !element.equals(issue.element) : issue.element != null) return false;
        if (description != null ? !description.equals(issue.description) : issue.description != null)
            return false;
        if (comment != null ? !comment.equals(issue.comment) : issue.comment != null) return false;
        if (time != null ? !time.equals(issue.time) : issue.time != null) return false;
        if (pictures != null ? !pictures.equals(issue.pictures) : issue.pictures != null)
            return false;
        if (latLng != null ? !latLng.equals(issue.latLng) : issue.latLng != null) return false;
        return issueId != null ? issueId.equals(issue.issueId) : issue.issueId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = distance;
        result = 31 * result + (subjectDistance != null ? subjectDistance.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (element != null ? element.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (pictures != null ? pictures.hashCode() : 0);
        result = 31 * result + (latLng != null ? latLng.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + reward;
        result = 31 * result + createdByUser;
        result = 31 * result + solvedByUser;
        result = 31 * result + (issueId != null ? issueId.hashCode() : 0);
        result = 31 * result + taskId;
        return result;
    }
}
