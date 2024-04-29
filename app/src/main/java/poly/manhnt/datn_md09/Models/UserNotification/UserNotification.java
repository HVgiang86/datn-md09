package poly.manhnt.datn_md09.Models.UserNotification;

import com.google.gson.annotations.SerializedName;

public class UserNotification {
    @SerializedName("_id")
    public String id;
    @SerializedName("id_user")
    public String userId;
    @SerializedName("statu_payload")
    public int payloadStatus;
    @SerializedName("payload")
    public String payload;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("date")
    public String date;
    @SerializedName("status")
    public boolean status;
    @SerializedName("image")
    public String image;


}
