package poly.manhnt.datn_md09.Models.ProductSizeColor;

import com.google.gson.annotations.SerializedName;

public class ProductSizeColor {
    @SerializedName("_id")
    public String sizeColorId;
    @SerializedName("size_id")
    public ProductSize size;
    @SerializedName("color_id")
    public ProductColor color;

    @SerializedName("quantity")
    public int quantity;
}

