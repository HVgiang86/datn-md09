package poly.manhnt.datn_md09.Views.DetailScreen;

import static poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity.EXTRA_PRODUCT_ID;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.DanhGiaAdapter;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Presenters.ProductDetailPresenter.ProductDetailContract;
import poly.manhnt.datn_md09.Presenters.ProductDetailPresenter.ProductDetailPresenter;
import poly.manhnt.datn_md09.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    DanhGiaAdapter danhGiaAdapter;
    private ProductResponse mProduct;
    private ProductDetailPresenter presenter;
    private String mProductId;
    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        presenter = new ProductDetailPresenter();
        presenter.setView(this);

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        if (productId != null && !productId.isEmpty()) {
            mProductId = productId;
            presenter.getProduct(productId);
            presenter.getComment(productId);
            //get Product Detail
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onGetProductSuccess(ProductResponse product) {
        Log.d("HAHA", "on get success");
        mProduct = product;
        mBinding.textPrice.setText("VNĐ " + product.price);
        mBinding.textName.setText(product.name);
        mBinding.textDesc.setText(product.description);
    }

    @Override
    public void onGetProductFail(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Load product detail fail!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onGetCommentSuccess(List<ProductComment> productComments) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        danhGiaAdapter = new DanhGiaAdapter(this, productComments);
        mBinding.recyclerDanhGia.setLayoutManager(layoutManager);
        mBinding.recyclerDanhGia.setAdapter(danhGiaAdapter);
        danhGiaAdapter.notifyDataSetChanged();

        int soLuongDanhGia = productComments.size();
        float sum = 0;
        for (ProductComment pc : productComments) {
            sum += pc.rating;
        }
        float trungBinh = sum / soLuongDanhGia;
        String rounded = new DecimalFormat("#.#").format(trungBinh);
        mBinding.textRating.setText(rounded + "/" + "5");
        mBinding.textRating2.setText(rounded + "/" + "5");

        mBinding.textCommentCount.setText(Integer.toString(soLuongDanhGia));
        if (soLuongDanhGia == 0) {
            mBinding.containerComment.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGetCommentFail(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Load product detail fail!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initProductCommentView() {

    }
}