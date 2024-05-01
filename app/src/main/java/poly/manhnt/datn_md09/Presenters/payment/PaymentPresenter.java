package poly.manhnt.datn_md09.Presenters.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import poly.manhnt.datn_md09.Models.MessageResponse;
import poly.manhnt.datn_md09.Models.payment.PaymentRequest;
import poly.manhnt.datn_md09.Models.payment.VnpResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import poly.manhnt.datn_md09.api.RetrofitOderClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentPresenter implements PaymentContract.Presenter {
    private final PaymentContract.View view;

    public PaymentPresenter(PaymentContract.View view) {
        this.view = view;
    }

    @Override
    public void createOnlinePayment(String uid, String[] cartIds, int amount, String discountId) {
        try {
            PaymentRequest request = new PaymentRequest();

            List<String> list = new ArrayList<>();
            Collections.addAll(list, cartIds);
            request.idCarts = list;
            request.amount = amount;
            request.discountId = discountId;
            RetrofitOderClient.getInstance().create(ApiService.class).onlinePayment(uid, request).enqueue(new Callback<VnpResponse>() {
                @Override
                public void onResponse(Call<VnpResponse> call, Response<VnpResponse> response) {
                    if (response.isSuccessful()) {
                        view.onCreateOnlinePaymentSuccess(response.body().paymentUrl);

                    } else {
                        view.onCreateOnlinePaymentFail(new Exception("Fail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<VnpResponse> call, Throwable t) {
                    view.onCreateOnlinePaymentFail(new Exception("Fail: " + t.getMessage()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.onCreateOnlinePaymentFail(e);
        }
    }

    @Override
    public void createBill(String uid, String[] cartIds, int amount, String discountId) {
        try {
            PaymentRequest request = new PaymentRequest();

            List<String> list = new ArrayList<>();
            Collections.addAll(list, cartIds);
            request.idCarts = list;
            request.amount = amount;
            request.discountId = discountId;
            RetrofitClient.getInstance().create(ApiService.class).addBill(uid, request).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    if (response.isSuccessful()) {
                        view.onCreateBillSuccess();
                    } else {
                        view.onCreateBillFail();
                    }
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    t.printStackTrace();
                    view.onCreateBillFail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.onCreateBillFail();
        }
    }
}
