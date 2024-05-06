package poly.manhnt.datn_md09.Presenters.Rating;

import java.util.List;

import poly.manhnt.datn_md09.Models.Bill.BillResponse;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingPresenter implements RatingContract.Presenter {
    RatingContract.View view = null;

    public RatingPresenter(RatingContract.View view) {
        this.view = view;
    }

    @Override
    public void getRating(String productId) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getProductComment(productId).enqueue(new Callback<List<ProductComment>>() {
                @Override
                public void onResponse(Call<List<ProductComment>> call, Response<List<ProductComment>> response) {
                    if (response.isSuccessful()) {
                        if (view != null) view.onGetRatingSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<ProductComment>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBillList(String uid) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getBillList(uid).enqueue(new Callback<BillResponse>() {
                @Override
                public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            view.onGetBillListSuccess(response.body().billList);
                        } else {
                            view.onGetBillListError(new Exception(response.message()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.onGetBillListError(e);
                    }
                }

                @Override
                public void onFailure(Call<BillResponse> call, Throwable t) {
                    t.printStackTrace();
                    view.onGetBillListError(new Exception(t.getMessage()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.onGetBillListError(e);
        }
    }
}
