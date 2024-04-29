package poly.manhnt.datn_md09.Models.cart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse {
    @SerializedName("message")
    public String message;
    @SerializedName("listCart")
    public List<Cart> cartList;
}
