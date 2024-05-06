package poly.manhnt.datn_md09.Models.ProductSearch;

import java.util.ArrayList;

import poly.manhnt.datn_md09.Models.ProductCategory;
import poly.manhnt.datn_md09.Models.ProductResponse;

public class ProductSearch {
    public String _id;
    public String name;
    public String description;
    public ArrayList<String> image;
    public String category_id;
    public int price;
    public Integer discount;
    public String createdAt;
    public int __v;

    public ProductResponse copy() {
        ProductResponse pr = new ProductResponse();
        pr._id = _id;
        pr.name = name;
        pr.price = price;
        pr.__v = __v;
        pr.category_id = new ProductCategory();
        pr.category_id._id = category_id;
        pr.createdAt = createdAt;
        pr.description = description;
        pr.image = image;
        pr.discount = discount;

        return pr;
    }

}
