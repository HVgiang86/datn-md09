package poly.manhnt.datn_md09.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryIdResponse {
    @SerializedName("listCategorys")
    public List<ProductCategory> categories;
}
