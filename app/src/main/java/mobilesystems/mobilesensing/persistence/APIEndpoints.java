package mobilesystems.mobilesensing.persistence;

import java.util.List;

import mobilesystems.mobilesensing.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jesper on 03/11/2016.
 */

public interface APIEndpoints {

    @GET("user/{name}")
    Call<User> getUser(@Path("name") String userName);

    @GET("user")
    Call<List<User>> getUsers();
}
