package poly.manhnt.datn_md09.Presenters.bill;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import poly.manhnt.datn_md09.Models.Bill.BillResponse;
import poly.manhnt.datn_md09.Models.Bill.BillStatusRequest;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillPresenter implements BillContract.Presenter {
    private final BillContract.View view;

    public BillPresenter(BillContract.View view) {
        this.view = view;
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

    @Override
    public void updateBillStatus(String billId, int status) {
        try {
            BillStatusRequest request = new BillStatusRequest();
            request.status = status;
            RetrofitClient.getInstance().create(ApiService.class).updateBillStatus(billId, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        view.onUpdateBillStatusSuccess(billId, status);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadComment(String uid, String productId, String sizeColorId, String comment, String rating, List<MultipartBody.Part> images) {
        try {
            RequestBody requestUid = RequestBody.create(MediaType.parse("text/plain"), uid);
            RequestBody requestProductId = RequestBody.create(MediaType.parse("text/plain"), productId);
            RequestBody requestSizeColorId = RequestBody.create(MediaType.parse("text/plain"), sizeColorId);
            RequestBody requestComment = RequestBody.create(MediaType.parse("text/plain"), comment);
            RequestBody requestRating = RequestBody.create(MediaType.parse("text/plain"), rating);

            RetrofitClient.getInstance().create(ApiService.class).addComment(images, requestUid, requestProductId, requestSizeColorId, requestComment, requestRating).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) view.onUploadCommentSuccess();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
