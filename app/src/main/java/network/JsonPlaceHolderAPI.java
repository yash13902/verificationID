package network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {

    @POST("testIdToken")
    Call<UserInfo> postUserInfo(@Header("Authorization") String token);
}
