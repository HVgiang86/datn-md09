package poly.manhnt.datn_md09.Models.cart;

import java.util.ArrayList;

import poly.manhnt.datn_md09.Models.ProductCategory;

public class ProductCart {
    public String _id;
    public String name;
    public ArrayList<String> image;
    public int price;
    public Integer discount;

    public int getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }
}
