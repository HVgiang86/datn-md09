package poly.manhnt.datn_md09.Presenters.ProductDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductResponse;

public interface ProductDetailContract {
    interface View {
        void onGetProductSuccess(ProductResponse product);

        void onGetProductFail(Exception e);

        void onGetCommentSuccess(List<ProductComment> productComments);

        void onGetCommentFail(Exception e);
    }

    interface Presenter{
        void setView(ProductDetailContract.View view);
        void getProduct(String productId);
        void getComment(String productId);
    }
}
