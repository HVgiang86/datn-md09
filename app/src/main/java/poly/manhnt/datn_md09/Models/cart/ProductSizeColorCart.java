package poly.manhnt.datn_md09.Models.cart;

import com.google.gson.annotations.SerializedName;

import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductColor;
import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductSize;

public class ProductSizeColorCart {
    @SerializedName("_id")
    public String sizeColorId;
    @SerializedName("size_id")
    public ProductSize size;
    @SerializedName("color_id")
    public ProductColor color;

    @SerializedName("product_id") public ProductCart product;
    @SerializedName("quantity")
    public int quantity;
}
