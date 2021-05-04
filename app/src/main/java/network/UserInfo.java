package network;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public UserInfo(String message) {
        this.message = message;
    }
}
