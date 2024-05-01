package poly.manhnt.datn_md09.Models.UserAddress;

import com.google.gson.annotations.SerializedName;

public class AddAddressRequest {
    @SerializedName("idUser")
    String uid;
    @SerializedName("address")
    String address;
    @SerializedName("specificAddres")
    String addressDetail;

    public AddAddressRequest(String uid, String address, String addressDetail) {
        this.uid = uid;
        this.address = address;
        this.addressDetail = addressDetail;
    }
}
