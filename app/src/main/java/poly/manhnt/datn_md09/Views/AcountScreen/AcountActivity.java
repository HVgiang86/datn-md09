package poly.manhnt.datn_md09.Views.AcountScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import poly.manhnt.datn_md09.Const.AppConfig;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Views.CartScreen.CartActivity;
import poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity;
import poly.manhnt.datn_md09.Views.NotifiScreen.NotifiActivity;
import poly.manhnt.datn_md09.Views.Rating.RatingActivity;
import poly.manhnt.datn_md09.Views.UpdateProfile.UpdateProfileActivity;
import poly.manhnt.datn_md09.Views.bill.BillListActivity;
import poly.manhnt.datn_md09.databinding.ActivityAcountBinding;

public class AcountActivity extends AppCompatActivity {
    private ActivityAcountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAcountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.textName.setText(DataManager.getInstance().getUserLogin.fullname);

        binding.buttonCart.setOnClickListener(v -> switchScreen(CartActivity.class));

        String avtImageUrl = AppConfig.API_URL + DataManager.getInstance().getUserLogin.avata;
        Glide.with(this).load(avtImageUrl).centerCrop().into(binding.imageAvatar);

        binding.buttonHome.setOnClickListener(v -> {
            switchScreen(HomeActivity.class);
        });
        binding.buttonNotification.setOnClickListener(v -> {
            switchScreen(NotifiActivity.class);
        });

        binding.buttonOrderWaitConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra(BillListActivity.EXTRA_SELECTED_TAB, BillListActivity.BillTab.PENDING.name());
            startActivity(intent);
        });

        binding.buttonOrderWaitTake.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra(BillListActivity.EXTRA_SELECTED_TAB, BillListActivity.BillTab.PAID.name());
            startActivity(intent);
        });

        binding.buttonOrderDelivering.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra(BillListActivity.EXTRA_SELECTED_TAB, BillListActivity.BillTab.DELIVERY.name());
            startActivity(intent);
        });

        binding.buttonOrderRate.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra(BillListActivity.EXTRA_SELECTED_TAB, BillListActivity.BillTab.DELIVERED.name());
            startActivity(intent);
        });

        binding.containerMyRating.setOnClickListener(v -> {
            Intent intent = new Intent(this, RatingActivity.class);
            startActivity(intent);
        });

        binding.buttonOrderCancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            intent.putExtra(BillListActivity.EXTRA_SELECTED_TAB, BillListActivity.BillTab.CANCELLED.name());
            startActivity(intent);
        });

        binding.containerOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, BillListActivity.class);
            startActivity(intent);
        });

        binding.buttonSetting.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateProfileActivity.class);
            startActivity(intent);
        });

        binding.textAccountSetting.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateProfileActivity.class);
            startActivity(intent);
        });
    }

    private <T> void switchScreen(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}