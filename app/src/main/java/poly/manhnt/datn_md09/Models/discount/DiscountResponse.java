package poly.manhnt.datn_md09.Models.discount;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscountResponse {
    @SerializedName("listdiscount")
    public List<UserDiscount> discountList;
}
