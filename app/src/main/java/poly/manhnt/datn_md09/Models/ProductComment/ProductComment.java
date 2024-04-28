package poly.manhnt.datn_md09.Models.ProductComment;

import java.util.List;

public class ProductComment {
    public String _id;
    public List<String> images;
    public String product_id;
    public UserCommentInfo user_id = null;
    public String comment;
    public float rating;
    public String date;
    public int __v;

    public ProductCommentDetail product_detail_id;
}


