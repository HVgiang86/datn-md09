package poly.manhnt.datn_md09.Views.bill;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import okhttp3.MultipartBody;
import poly.manhnt.datn_md09.Adapters.BillListAdapter;
import poly.manhnt.datn_md09.Adapters.BillListPageAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.Presenters.bill.BillContract;
import poly.manhnt.datn_md09.Presenters.bill.BillPresenter;
import poly.manhnt.datn_md09.Views.BillDetail.BillDetailActivity;
import poly.manhnt.datn_md09.Views.popup.LoadingPopupFragment;
import poly.manhnt.datn_md09.Views.popup.RatingPopupFragment;
import poly.manhnt.datn_md09.databinding.ActivityBillListBinding;

public class BillListActivity extends AppCompatActivity implements BillContract.View, BillListAdapter.OnBillItemClickListener, RatingPopupFragment.OnButtonAcceptClickListener {
    public static final String EXTRA_SELECTED_TAB = "selectedTab";
    private final String[] tabNames = {"Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao", "Huỷ"};
    private ActivityBillListBinding binding;
    private BillContract.Presenter presenter;
    private BillListPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new BillPresenter(this);
        presenter.getBillList(DataManager.getInstance().getUserLogin.idUser);
        toggleLoadingPopup(true);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new BillListPageAdapter();
        adapter.setListener(this);
        binding.containerPage.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.containerPage, (tab, i) -> tab.setText(tabNames[i]));
        tabLayoutMediator.attach();

        String selectedTabStr = getIntent().getStringExtra(EXTRA_SELECTED_TAB);
        int selectedTab = 0;
        if (selectedTabStr != null) {
            selectedTab = BillTab.valueOf(selectedTabStr).ordinal();
        }

        binding.tabLayout.getTabAt(selectedTab).select();
        binding.containerPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

    }

    private void toggleLoadingPopup(boolean isLoading) {
        if (isLoading) {
            new LoadingPopupFragment().show(getSupportFragmentManager(), "LoadingPopupFragment.TAG");
        } else {
            LoadingPopupFragment fragment = (LoadingPopupFragment) getSupportFragmentManager().findFragmentByTag("LoadingPopupFragment.TAG");
            if (fragment != null) {
                fragment.dismiss();
            }
        }
    }

    @Override
    public void onGetBillListSuccess(List<Bill> billList) {
        toggleLoadingPopup(false);
        adapter.updateData(billList);
    }

    @Override
    public void onGetBillListError(Exception e) {
        toggleLoadingPopup(false);
        finish();
        e.printStackTrace();
    }

    @Override
    public void onUpdateBillStatusSuccess(String billId, int status) {
        presenter.getBillList(DataManager.getInstance().getUserLogin.idUser);
        toggleLoadingPopup(true);
    }

    @Override
    public void onUploadCommentSuccess() {
        System.out.println("Comment success");
    }

    @Override
    public void onCancelClick(Bill bill) {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("Bạn xác nhận huỷ đơn hàng?").setNegativeButton("Không", (dialog1, which) -> {
            dialog1.dismiss();
        }).setPositiveButton("Có", (dialog1, which) -> {
            presenter.updateBillStatus(bill.id, 9);
        }).create();
        dialog.show();
    }

    @Override
    public void onContactShopClick(Bill bill) {
        Toast.makeText(this, "contact click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRateClick(Bill bill) {
        RatingPopupFragment ratingPopupFragment = new RatingPopupFragment(this, bill);
        ratingPopupFragment.show(getSupportFragmentManager(), "RatingPopupFragment.TAG");
    }

    @Override
    public void onReorder(Bill bill) {

    }

    @Override
    public void onViewBillClick(Bill bill) {
        openBillDetail(bill.id);
    }

    private void openBillDetail(String billId) {
        Intent intent = new Intent(this, BillDetailActivity.class);
        intent.putExtra(BillDetailActivity.KEY_BILL_ID, billId);
        startActivity(intent);
    }

    @Override
    public void onOrderSuccessButtonAcceptClick(List<MultipartBody.Part> imageParts, String rating, String comment, String productId, String productSizeColor) {
        System.out.println("Upload comment");
        presenter.uploadComment(DataManager.getInstance().getUserLogin.idUser, productId, productSizeColor, comment, rating, imageParts);
        //Upload comment
        Toast.makeText(this, "Rated!", Toast.LENGTH_SHORT).show();
    }

    public enum BillTab {
        PENDING, PAID, DELIVERY, DELIVERED, CANCELLED
    }

}