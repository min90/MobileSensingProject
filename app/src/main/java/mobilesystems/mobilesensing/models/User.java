package mobilesystems.mobilesensing.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mobilesystems.sugarorm.SugarApp;
import mobilesystems.sugarorm.SugarRecord;


/**
 * Created by Jesper on 27/10/2016.
 */

public class User extends SugarRecord {

    private String userID;
    @SerializedName("phone")
    private String userPhone;
    @SerializedName("email")
    private String userEmail;
    @SerializedName("name")
    private String userName;
    @SerializedName("password")
    private String userPassword;

    public User() {
    }

    public User(String userID, String userPhone, String userEmail, String userName, String userPassword) {
        this.userID = userID;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userPhone != null ? !userPhone.equals(user.userPhone) : user.userPhone != null)
            return false;
        if (userEmail != null ? !userEmail.equals(user.userEmail) : user.userEmail != null)
            return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null)
            return false;
        return userPassword != null ? userPassword.equals(user.userPassword) : user.userPassword == null;

    }

    @Override
    public int hashCode() {
        int result = userPhone != null ? userPhone.hashCode() : 0;
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        return result;
    }
}
