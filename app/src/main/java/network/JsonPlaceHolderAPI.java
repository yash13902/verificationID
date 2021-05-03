package network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface JsonPlaceHolderAPI {

    @GET("name")
    static Call<UserInfo> getUserInfo(@Header("Token") String token) {
        return null;
    }
}
