package poly.manhnt.datn_md09.Presenters.Notification;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNotificationPresenter implements UserNotificationContract.Presenter {
    private final UserNotificationContract.View mView;

    public UserNotificationPresenter(UserNotificationContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getNotification(String uid) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getNotification(uid).enqueue(new Callback<List<UserNotification>>() {
                @Override
                public void onResponse(Call<List<UserNotification>> call, Response<List<UserNotification>> response) {
                    if (response.isSuccessful()) {
                        mView.onGetUserNotificationSuccess(response.body());
                    } else
                        mView.onGetUserNotificationFail(new Exception("Fail" + response.message()));
                }

                @Override
                public void onFailure(Call<List<UserNotification>> call, Throwable t) {
                    t.printStackTrace();
                    mView.onGetUserNotificationFail(new Exception("Fail" + t.getMessage()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mView.onGetUserNotificationFail(e);
        }
    }

    @Override
    public void markNotificationRead(String notificationId) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).markReadNotification(notificationId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful())
                        mView.onMarkNotificationReadSuccess(notificationId);
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
