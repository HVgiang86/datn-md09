package poly.manhnt.datn_md09.Models.ProductSearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import poly.manhnt.datn_md09.Models.ProductResponse;

public class ProductSearchResponse {
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<ProductSearch> data;
}
