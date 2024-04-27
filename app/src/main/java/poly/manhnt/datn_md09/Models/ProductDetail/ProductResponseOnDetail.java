package poly.manhnt.datn_md09.Models.ProductDetail;

import java.util.ArrayList;

import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.ProductResponse;

public class ProductResponseOnDetail {
    public String _id;
    public String name;
    public String description;
    public ArrayList<String> image;
    public String category_id;
    public int price;
    public Object discount;
    public String createdAt;
    public int __v;
    public String updatedAt;

    public ProductResponse copy() {
        ProductResponse rp = new ProductResponse();
        rp._id = _id;
        rp.name = name;
        rp.description = description;
        rp.image = image;
        rp.category_id = new CategoryIdResponse();
        rp.category_id._id = category_id;
        rp.price = price;
        rp.discount = discount;
        rp.createdAt = createdAt;
        rp.__v = __v;
        rp.updatedAt = updatedAt;
        return rp;
    }
}
