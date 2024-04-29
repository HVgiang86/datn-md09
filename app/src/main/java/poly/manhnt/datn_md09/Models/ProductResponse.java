package poly.manhnt.datn_md09.Models;

import java.util.ArrayList;

public class ProductResponse {
    public String _id;
    public String name;
    public String description;
    public ArrayList<String> image;
    public ProductCategory category_id;
    public int price;
    public Integer discount;

    public transient int finalPrice;
    public String createdAt;
    public int __v;

    public int getPrice() {
        return finalPrice;
    }

}
