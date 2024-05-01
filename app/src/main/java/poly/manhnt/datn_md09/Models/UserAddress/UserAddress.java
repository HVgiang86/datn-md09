package poly.manhnt.datn_md09.Models.UserAddress;

import com.google.gson.annotations.SerializedName;

public class UserAddress {
    @SerializedName("_id")
    public String id;
    @SerializedName("specific_addres")
    public String addressDetail;
    @SerializedName("address")
    public String address;
}
