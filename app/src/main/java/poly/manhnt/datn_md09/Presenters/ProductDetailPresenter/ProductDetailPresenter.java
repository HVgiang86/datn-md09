package poly.manhnt.datn_md09.Presenters.ProductDetailPresenter;

import java.util.List;
import java.util.Objects;

import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductDetail.ProductDetailResponse;
import poly.manhnt.datn_md09.Models.ProductDetail.ProductResponseOnDetail;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {
    private ProductDetailContract.View view;

    @Override
    public void setView(ProductDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void getProduct(String productId) {
        RetrofitClient.getInstance().create(ApiService.class).getProductDetail(productId).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ProductResponseOnDetail productResponseOnDetail = response.body().objProduct;
                        ProductResponse pr = productResponseOnDetail.copy();

                        view.onGetProductSuccess(pr);
                    } else {
                        view.onGetProductFail(new Exception("No product detail found!"));
                    }
                } else {
                    view.onGetProductFail(new Exception("Get fail! Status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {

                view.onGetProductFail(new Exception("Fail: " + Objects.requireNonNull(t.getCause()) + "\t" + t.getMessage()));
            }
        });
    }

    @Override
    public void getComment(String productId) {
        RetrofitClient.getInstance().create(ApiService.class).getProductComment(productId).enqueue(new Callback<List<ProductComment>>() {
            @Override
            public void onResponse(Call<List<ProductComment>> call, Response<List<ProductComment>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        view.onGetCommentSuccess(response.body());
                    } else {
                        view.onGetCommentFail(new Exception("No product comment detail found!"));
                    }
                } else {
                    view.onGetCommentFail(new Exception("Get fail! Status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ProductComment>> call, Throwable t) {
                view.onGetCommentFail(new Exception("Fail: " + Objects.requireNonNull(t.getCause()) + "\t" + t.getMessage()));
            }
        });
    }
}
