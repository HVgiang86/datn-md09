package poly.manhnt.datn_md09.Models.UserAddress;

import com.google.gson.annotations.SerializedName;

public class SetAddressRequest {
    @SerializedName("idUser")
    String uid;
    @SerializedName("idAddress")
    String addressId;

    public SetAddressRequest(String uid, String addressId) {
        this.uid = uid;
        this.addressId = addressId;
    }
}
