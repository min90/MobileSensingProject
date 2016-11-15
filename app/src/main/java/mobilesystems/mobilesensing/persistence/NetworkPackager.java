package mobilesystems.mobilesensing.persistence;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mobilesystems.mobilesensing.models.Challenge;
import mobilesystems.mobilesensing.models.Issue;
import mobilesystems.mobilesensing.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jesper on 03/11/2016.
 */

public class NetworkPackager {
    private static final String DEBUG_TAG = NetworkPackager.class.getSimpleName();
    private static final String BASE_URL = "https://sdu-api-mobile-sensing.herokuapp.com/";

    private Context context;

    public NetworkPackager(Context context) {
        this.context = context;
    }

    private Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        APIEndpoints apiEndpoints = getRetrofitInstance().create(APIEndpoints.class);
        Call<List<User>> call = apiEndpoints.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users.addAll(response.body());
                Log.d(DEBUG_TAG, "Users: " + users.toString());
                addNewUsersToDB(users);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(DEBUG_TAG, "Unable to contact api", t.getCause());
            }
        });
        return users;
    }

    public List<Issue> getIssues() {
        final List<Issue> issues = new ArrayList<>();
        APIEndpoints apiEndpoints = getRetrofitInstance().create(APIEndpoints.class);
        Call<List<Issue>> callForIssues = apiEndpoints.getIssues();
        callForIssues.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                issues.addAll(response.body());
                Log.d(DEBUG_TAG, "Issues: " + response.body());
                Log.d(DEBUG_TAG, "Issues: " + issues.toString());
                addNewIssuesToDB(issues);

            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                Log.e(DEBUG_TAG, "Unable to read response: " + t.getLocalizedMessage());
            }
        });
        return issues;
    }

    public List<Challenge> getChallenges() {
        final List<Challenge> challenges = new ArrayList<>();
        APIEndpoints apiEndpoints = getRetrofitInstance().create(APIEndpoints.class);
        Call<List<Challenge>> callForChallenges = apiEndpoints.getChallenges();
        callForChallenges.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                challenges.addAll(response.body());
                Log.d(DEBUG_TAG, "response: " + response.code());
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                Log.e(DEBUG_TAG, "Unable to read response: " + t.getLocalizedMessage());
            }
        });
        return challenges;
    }

    private void addNewUsersToDB(List<User> users) {
        List<User> allUsers = User.listAll(User.class);
        for (User user : users) {
            for (User user1 : allUsers) {
                if (!user.equals(user1)) {
                    user.save();
                }
            }
        }
    }

    private void addNewIssuesToDB(List<Issue> issues) {
        List<Issue> allIssues = Issue.listAll(Issue.class);
        for (Issue issue : issues) {
            for (Issue allIssue : allIssues) {
                if (!issue.equals(allIssue)){
                    issue.save();
                }
            }
        }
    }

    private void addNewChallengesToDb(List<Challenge> challenges) {
        List<Challenge> allChallenges = Challenge.listAll(Challenge.class);
        for (Challenge challenge : challenges) {
            for (Challenge challenge1 : allChallenges) {
                if (!challenge.equals(challenge1)){
                    challenge.save();
                }
            }
        }
    }
}
