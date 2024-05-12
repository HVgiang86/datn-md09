package poly.manhnt.datn_md09.Presenters.account;

import java.util.List;

import okhttp3.MultipartBody;

public interface AccountContract {
    interface View {
        void onChangePasswordSuccess();

        void onChangePasswordFailed(String message);

        void onChangeAvatarSuccess();
    }

    interface Presenter {
        void changePassword(String uid, String password, String newPassword);

        void changeAvatar(String uid, MultipartBody.Part images);
    }
}
