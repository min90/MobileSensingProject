package mobilesystems.mobilesensing.persistence;

import java.util.List;

import mobilesystems.mobilesensing.models.Challenge;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jesper on 03/11/2016.
 */

public interface APIEndpoints {

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String userId);

    @GET("user")
    Call<List<User>> getUsers();

    @POST("user")
    Call<User> createUser(@Body User user);

    @PATCH("user/{id}")
    Call<User> updateUser(@Body User user, @Path("id") String userId);

    @DELETE
    Call<User> deleteUser(@Path("id") String userID);

    @GET("challenge")
    Call<List<Challenge>> getChallenges();

    @GET("challenge/{id}")
    Call<Challenge> getChallenge(@Path("id") String challengeID);

    @POST("challenge")
    Call<User> createChallenge(@Body Challenge challenge);

    @PATCH("challenge/{id}")
    Call<Challenge> updateChallenge(@Body Challenge challenge, @Path("id") String challengeID);

    @DELETE
    Call<User> deleteChallenge(@Path("id") String challengeID);

    @GET("issue")
    Call<List<Issue>> getIssues();

    @GET("issue/{id}")
    Call<Issue> getIssue(@Path("id") String issueId);

    @POST("issue")
    Call<Issue> createIssue(@Body Issue issue);

    @PATCH("issue/{id}")
    Call<Issue> updateIssue(@Body Issue issue, @Path("id") String issueId);

    @DELETE("issue/{id}")
    Call<Issue> deleteIssue(@Path("id") String issueId);

}
