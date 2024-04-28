package poly.manhnt.datn_md09.Presenters.HomePresenter.ProductPresent;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Models.ProductSearch.ProductSearchResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPresenter implements ProductContract.Presenter {
    private final ProductContract.View mView;

    public ProductPresenter(ProductContract.View view) {
        mView = view;
    }

    @Override
    public void getProductPage(int page) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getListProduct(page).enqueue(new Callback<List<ProductResponse>>() {
                @Override
                public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                    if (response.isSuccessful()) {
                        mView.onGetProductPageSuccess(page, response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchProductByName(String name) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).searchByName(name).enqueue(new Callback<ProductSearchResponse>() {
                @Override
                public void onResponse(Call<ProductSearchResponse> call, Response<ProductSearchResponse> response) {
                    if (response.isSuccessful()) {
                        List<ProductResponse> products = new ArrayList();
                        response.body().data.forEach(productSearch -> {
                            products.add(productSearch.copy());
                        });
                        mView.onSearchProductSuccess(products);
                    }
                }

                @Override
                public void onFailure(Call<ProductSearchResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
