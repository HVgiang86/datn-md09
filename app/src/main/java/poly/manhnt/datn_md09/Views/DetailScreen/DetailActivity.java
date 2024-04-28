package poly.manhnt.datn_md09.Views.DetailScreen;

import static poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity.EXTRA_PRODUCT_ID;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.DanhGiaAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductSizeColor;
import poly.manhnt.datn_md09.Presenters.ProductDetailPresenter.ProductDetailContract;
import poly.manhnt.datn_md09.Presenters.ProductDetailPresenter.ProductDetailPresenter;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.ActivityDetailBinding;
import poly.manhnt.datn_md09.databinding.ItemBottomSheetAddCartBinding;

public class DetailActivity extends AppCompatActivity implements ProductDetailContract.View {
    private final Handler handler = new Handler(Looper.getMainLooper());
    DanhGiaAdapter danhGiaAdapter;
    private boolean isColorSelected = false;
    private boolean isSizeSelected = false;
    private boolean isGotSizeColorList = false;
    private List<ProductSizeColor> sizeColorList;
    private String selectedSize;
    private String selectedColor;
    private ProductResponse mProduct;
    private ProductDetailPresenter presenter;
    private String mProductId;
    private ActivityDetailBinding mBinding;
    private ItemBottomSheetAddCartBinding mBottomSheetBinding;
    private BottomSheetBehavior mSheetBehavior;
    private int currentPosition = 0;

    private AddToCartMode confirmCartMode;

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

            presenter.getProductSizeColor(productId);
            //get Size and Color
        }

        mBinding.buttonAddCart.setOnClickListener(v -> {
            if (isGotSizeColorList) mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            confirmCartMode = AddToCartMode.ADD_TO_CART;
        });

        mBinding.buttonBuyNow.setOnClickListener(v -> {
            confirmCartMode = AddToCartMode.BUY_NOW;
            if (isGotSizeColorList) mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        mBinding.buttonConfirmAddToCart.setOnClickListener(v -> {
            if (confirmCartMode == AddToCartMode.ADD_TO_CART) addToCart();
        });

        LinearLayout llBottomSheet = findViewById(R.id.bottom_sheet);

        mSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetProductSuccess(ProductResponse product) {
        Log.d("HAHA", "on get success");
        mProduct = product;
        mBinding.textPrice.setText("VNĐ " + product.price);
        mBinding.textName.setText(product.name);
        mBinding.textDesc.setText(product.description);

        //View Pager
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(mProduct.image);
        mBinding.viewPager.setAdapter(adapter);
        startAutoScroll(mProduct.image.size());
    }

    private void startAutoScroll(int itemCount) {
        if (itemCount == 0) return;
        handler.postDelayed(() -> {
            currentPosition++;
            if (currentPosition >= itemCount) {
                currentPosition = 0;
            }
            mBinding.viewPager.setCurrentItem(currentPosition);
            startAutoScroll(itemCount);
        }, 3000);
    }

    @Override
    public void onGetProductFail(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Load product detail fail!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addToCart() {
        for (ProductSizeColor psc : sizeColorList) {
            if (psc.size.sizeName.equals(selectedSize) && psc.color.colorName.equals(selectedColor)) {
                presenter.addToCart(DataManager.getInstance().getUserLogin.idUser, psc.sizeColorId);
            }
        }
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

    @Override
    public void onAddToCartSuccess() {
        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onAddToCartFail(Exception e) {
        Toast.makeText(this, "Không thể thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetProductSizeColorSuccess(List<ProductSizeColor> sizeColorList) {
        this.sizeColorList = sizeColorList;
        isGotSizeColorList = true;
        ArrayList<String> sizeList = new ArrayList();
        for (ProductSizeColor psc : sizeColorList) {
            sizeList.add(psc.size.sizeName);
        }

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerSize.setAdapter(sizeAdapter);

        mBinding.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSizeSelected = true;
                selectedSize = sizeList.get(position);
                ArrayList<String> colorList = new ArrayList();
                for (ProductSizeColor psc : sizeColorList) {
                    if (psc.size.sizeName.equals(selectedSize)) {
                        colorList.add(psc.color.colorName);
                    }
                }
                initColorChooseSpinner(colorList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBinding.spinnerColor.setAdapter(null);
                isSizeSelected = false;
            }
        });
    }

    private void initColorChooseSpinner(ArrayList<String> colors) {
        Log.d("HEHE", "Recreate color spinner");
        ArrayAdapter<String> colorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerColor.setAdapter(colorAdapter);
        colorAdapter.notifyDataSetChanged();

        mBinding.spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isColorSelected = true;
                selectedColor = colors.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isColorSelected = false;
            }
        });
    }

    @Override
    public void onGetProductSizeColorFail(Exception e) {
        Toast.makeText(this, "Fail to get Product size and color", Toast.LENGTH_SHORT).show();
        finish();
    }

    private enum AddToCartMode {
        ADD_TO_CART, BUY_NOW
    }
}