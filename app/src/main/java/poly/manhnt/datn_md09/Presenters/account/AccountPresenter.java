package poly.manhnt.datn_md09.Presenters.account;

import okhttp3.MultipartBody;
import poly.manhnt.datn_md09.Models.Account.ChangePasswordRequest;
import poly.manhnt.datn_md09.Models.MessageResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import poly.manhnt.datn_md09.api.RetrofitOderClient;
import poly.manhnt.datn_md09.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPresenter implements AccountContract.Presenter {
    private AccountContract.View view;

    public AccountPresenter(AccountContract.View view) {
        this.view = view;
    }

    @Override
    public void changePassword(String uid, String password, String newPassword) {
        try {
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.oldPassword = password;
            request.newPassword = password;
            RetrofitClient.getInstance().create(ApiService.class).changePassword(uid, request).enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    if (response.isSuccessful()) {
                        if (Utils.compare(response.body().msg, "Thay đổi mật khẩu thành công")) {
                            view.onChangePasswordSuccess();
                        } else {
                            view.onChangePasswordFailed(response.body().msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageResponse> call, Throwable t) {
                    t.printStackTrace();
                    view.onChangePasswordFailed(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeAvatar(String uid, MultipartBody.Part images) {
        try {
            RetrofitOderClient.getInstance().create(ApiService.class).changeAvatar(images, uid).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                        view.onChangeAvatarSuccess();
                    else
                        System.out.println(response.errorBody());
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
