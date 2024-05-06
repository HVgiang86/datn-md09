package poly.manhnt.datn_md09.Models.Bill;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.Models.discount.UserDiscount;

public class Bill {

    @SerializedName("_id")
    public String id;
    @SerializedName("discount_data")
    public UserDiscount discount;

    @SerializedName("user_id")
    public UserData user;

    @SerializedName("cart_id")
    public List<Cart> carts;

    @SerializedName("payments")
    public int paymentCode = 1;

    @SerializedName("status")
    public int statusCode = 0;

    @SerializedName("total_amount")
    public int totalAmount;

    @SerializedName("createdAt")
    public String createdAt;

    public Bill(String id, UserDiscount discount, List<Cart> carts, int paymentCode, int statusCode, int totalAmount) {
        this.id = id;
        this.discount = discount;
        this.carts = carts;
        this.paymentCode = paymentCode;
        this.statusCode = statusCode;
        this.totalAmount = totalAmount;
    }
}
