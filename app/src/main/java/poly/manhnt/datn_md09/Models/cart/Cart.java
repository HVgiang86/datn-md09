package poly.manhnt.datn_md09.Models.cart;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("_id")
    public String _id;
    @SerializedName("product_id")
    public ProductSizeColorCart sizeColor;
    @SerializedName("status")
    public String status;

    public int quantity = 1;

}
