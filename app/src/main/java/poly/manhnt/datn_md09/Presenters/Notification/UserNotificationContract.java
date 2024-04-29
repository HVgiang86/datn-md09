package poly.manhnt.datn_md09.Presenters.Notification;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;

public interface UserNotificationContract {
    interface View {
        void onGetUserNotificationSuccess(List<UserNotification> notifications);

        void onGetUserNotificationFail(Exception e);

        void onMarkNotificationReadSuccess(String notificationId);
    }

    interface Presenter {
        void getNotification(String uid);
        void markNotificationRead(String notificationId);
    }
}
