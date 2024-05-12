package poly.manhnt.datn_md09.Models.Account;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {
    @SerializedName("password")
    public String oldPassword;

    @SerializedName("newPassword")
    public String newPassword;
}
