package poly.manhnt.datn_md09.Presenters.HomePresenter.ProductPresent;

import java.util.List;

import poly.manhnt.datn_md09.Models.ProductResponse;

public interface ProductContract {

    interface View {
        void onGetProductPageSuccess(int page, List<ProductResponse> productResponseList);

        void onSearchProductSuccess(List<ProductResponse> productResponseList);
    }

    interface Presenter {
        void getProductPage(int page);

        void searchProductByName(String name);
    }
}
