package poly.manhnt.datn_md09.Presenters.BillDetail;

import poly.manhnt.datn_md09.Models.Bill.BillDetailResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailPresenter implements BillDetailContract.Presenter {
    private BillDetailContract.View view = null;

    public BillDetailPresenter(BillDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void getBillDetail(String billId) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getBillDetail(billId).enqueue(new Callback<BillDetailResponse>() {
                @Override
                public void onResponse(Call<BillDetailResponse> call, Response<BillDetailResponse> response) {
                    if (response.isSuccessful()) {
                        view.onGetBillDetailSuccess(response.body().bill);

                    } else {
                        if (view != null) view.onGetBillDetailError(response.message());
                    }
                }

                @Override
                public void onFailure(Call<BillDetailResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (view != null) view.onGetBillDetailError(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) view.onGetBillDetailError(e.getMessage());
        }
    }

}
