package mobilesystems.mobilesensing.persistence;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("unchecked")
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
}
