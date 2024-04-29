package poly.manhnt.datn_md09.Presenters.ProductDetailPresenter;

import java.util.List;

import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductSizeColor;

public interface ProductDetailContract {
    interface View {
        void onGetProductSuccess(ProductResponse product);

        void onGetProductFail(Exception e);

        void onGetCommentSuccess(List<ProductComment> productComments);

        void onGetCommentFail(Exception e);

        void onAddToCartSuccess();

        void onAddToCartFail(Exception e);

        void onGetProductSizeColorSuccess(List<ProductSizeColor> sizeColorList);

        void onGetProductSizeColorFail(Exception e);
    }

    interface Presenter {
        void setView(ProductDetailContract.View view);

        void getProduct(String productId);

        void getComment(String productId);

        void addToCart(String productId, String sizeColorId, int quantity);

        void getProductSizeColor(String productId);
    }
}
