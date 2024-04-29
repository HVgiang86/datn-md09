package poly.manhnt.datn_md09.Views.NotifiScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import poly.manhnt.datn_md09.Adapters.NotifiAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;
import poly.manhnt.datn_md09.Presenters.Notification.UserNotificationContract;
import poly.manhnt.datn_md09.Presenters.Notification.UserNotificationPresenter;
import poly.manhnt.datn_md09.Views.AcountScreen.AcountActivity;
import poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity;
import poly.manhnt.datn_md09.databinding.ActivityNotifiBinding;


public class NotifiActivity extends AppCompatActivity implements UserNotificationContract.View {
    private NotifiAdapter notifiAdapter;
    private UserNotificationPresenter presenter;
    private ActivityNotifiBinding binding;
    private List<UserNotification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new UserNotificationPresenter(this);
        presenter.getNotification(DataManager.getInstance().getUserLogin.idUser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerNotifi.setLayoutManager(layoutManager);

        binding.btnHomeScreen.setOnClickListener(v -> {
            switchScreen(HomeActivity.class);
        });
        binding.btnAccountScreen.setOnClickListener(v -> {
            switchScreen(AcountActivity.class);
        });

        binding.buttonMarkRead.setOnClickListener(v -> {
            for (UserNotification notification : notifications) {
                presenter.markNotificationRead(notification.id);
            }
            Toast.makeText(this, "Mark all notification as read", Toast.LENGTH_SHORT).show();
        });
    }

    private <T> void switchScreen(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetUserNotificationSuccess(List<UserNotification> notifications) {
        notifiAdapter = new NotifiAdapter(this, notifications);
        this.notifications = notifications;
        binding.recyclerNotifi.setAdapter(notifiAdapter);
        notifiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetUserNotificationFail(Exception e) {
        Toast.makeText(this, "Load notification fail!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onMarkNotificationReadSuccess(String notificationId) {
        notifiAdapter.updateNotification(notificationId);
    }
}