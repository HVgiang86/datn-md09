package poly.manhnt.datn_md09.Views.Rating;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.DanhGiaAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.Presenters.Rating.RatingContract;
import poly.manhnt.datn_md09.Presenters.Rating.RatingPresenter;
import poly.manhnt.datn_md09.Views.popup.LoadingPopupFragment;
import poly.manhnt.datn_md09.databinding.ActivityRatingBinding;

public class RatingActivity extends AppCompatActivity implements RatingContract.View {

    private final List<String> productIdList = new ArrayList<>();
    private ActivityRatingBinding binding;
    private RatingPresenter ratingPresenter;
    private DanhGiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ratingPresenter = new RatingPresenter(this);

        if (productIdList == null) {
            finish();
            return;
        }

        adapter = new DanhGiaAdapter(this, new ArrayList<>());
        binding.recycler.setAdapter(adapter);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        initData();

    }

    private void initData() {
        toggleLoadingPopup(true);
        ratingPresenter.getBillList(DataManager.getInstance().getUserLogin.idUser);
    }

    @Override
    public void onGetRatingSuccess(List<ProductComment> productComments) {
        List<ProductComment> comments = new ArrayList<>();
        for (ProductComment productComment : productComments) {
            if (productComment.user_id != null) {
                if (productComment.user_id._id.equals(DataManager.getInstance().getUserLogin.idUser)) {
                    comments.add(productComment);
                }
            }
        }
        for (ProductComment comment : comments) {
            adapter.addComment(comment);
        }
    }

    @Override
    public void onGetBillListSuccess(List<Bill> billList) {
        toggleLoadingPopup(false);
        for (Bill bill : billList) {
            for (Cart cart : bill.carts) {
                String id = cart.sizeColor.product._id;
                if (!productIdList.contains(id)) {
                    productIdList.add(id);
                    adapter.addProductCart(cart.sizeColor.product);
                }
            }
        }

        for (String productId : productIdList) {
            ratingPresenter.getRating(productId);
        }
    }

    @Override
    public void onGetBillListError(Exception e) {
        finish();
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
}