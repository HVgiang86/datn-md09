package poly.manhnt.datn_md09.Views.CartScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.CartAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.Models.discount.UserDiscount;
import poly.manhnt.datn_md09.Presenters.UserDiscount.UserDiscountContract;
import poly.manhnt.datn_md09.Presenters.UserDiscount.UserDiscountPresenter;
import poly.manhnt.datn_md09.Presenters.cart.CartContract;
import poly.manhnt.datn_md09.Presenters.cart.CartPresenter;
import poly.manhnt.datn_md09.Views.DetailScreen.DetailActivity;
import poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity;
import poly.manhnt.datn_md09.Views.PayScreen.PayActivity;
import poly.manhnt.datn_md09.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemInteractListener, CartContract.View, UserDiscountContract.View {

    public static final String KEY_CART_ID_ARRAY = "KEY_CART_ID_ARRAY";
    public static final String KEY_AMOUNT = "KEY_AMOUNT";
    public static final String KEY_DISCOUNT_ID = "KEY_DISCOUNT_ID";
    private int discount = 0;
    private String discountId;
    private ActivityCartBinding binding;
    private CartPresenter presenter;
    private UserDiscountPresenter discountPresenter;
    private int totalPrice;
    private int finalPrice = 0;
    private CartAdapter adapter;
    private List<UserDiscount> discountList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CartPresenter(this);
        discountPresenter = new UserDiscountPresenter(this);

        adapter = new CartAdapter();
        adapter.setOnItemInteractListener(this);
        binding.recycler.setAdapter(adapter);
        initData();

        binding.buttonAddMoreProduct.setOnClickListener(v -> finish());
        binding.buttonOpenChat.setOnClickListener(v -> {
        });
        binding.buttonOrderConfirm.setOnClickListener(v -> {
            doPayment();
        });

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void doPayment() {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra(KEY_AMOUNT, finalPrice);
        intent.putExtra(KEY_DISCOUNT_ID, discountId);
        String[] idCarts = adapter.getCartIdArray();
        intent.putExtra(KEY_CART_ID_ARRAY, idCarts);
        startActivity(intent);
    }

    private void initData() {
        presenter.getCartList(DataManager.getInstance().getUserLogin.idUser);
        discountPresenter.getUserDiscount(DataManager.getInstance().getUserLogin.idUser);
    }

    @Override
    public void onGetCartListSuccess(List<Cart> carts) {
        adapter.updateData(carts);
    }

    @Override
    public void onGetCartFail(Exception e) {
        Toast.makeText(this, "Fail to load your cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateCartItemSuccess(String cartId, int quantity) {
        adapter.updateCartItem(cartId, quantity);
    }

    @Override
    public void onUpdateCartFail(Exception e) {
        Toast.makeText(this, "Fail to update your cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuantityMinus(String cartId, int afterQuantity) {
        if (afterQuantity > 0)
            presenter.updateCartItem(DataManager.getInstance().getUserLogin.idUser, cartId, afterQuantity);
        else presenter.deleteCartItem(cartId);
    }

    @Override
    public void onQuantityPlus(String cartId, int afterQuantity) {
        presenter.updateCartItem(DataManager.getInstance().getUserLogin.idUser, cartId, afterQuantity);
    }

    @Override
    public void onItemClick(String productId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(HomeActivity.EXTRA_PRODUCT_ID, productId);
        startActivity(intent);
    }

    @Override
    public void onTotalPriceChange(int totalPrice) {
        this.totalPrice = totalPrice;
        updateTotalPriceText();
    }


    @SuppressLint("SetTextI18n")
    private void updateTotalPriceText() {
        finalPrice = (totalPrice <= discount) ? 0 : totalPrice - discount;
        String s = "Thanh toán: " + finalPrice + "đ";
        binding.buttonOrderConfirm.setText(s);
    }

    @Override
    public void onGetUserDiscountSuccess(List<UserDiscount> discountList) {
        System.out.println("Get discount success");
        this.discountList = discountList;

        ArrayList<String> discountString = new ArrayList<>();
        for (UserDiscount discount : discountList) {
            discountString.add("Giảm " + discount.discount + "đ");
        }
        discountString.add("Không giảm");
        int size = discountList.size();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, discountString);
        binding.spinnerUserDiscount.setAdapter(adapter1);

        binding.spinnerUserDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == size) {
                    discount = 0;
                    discountId = "";
                } else {
                    discountId = discountList.get(position).id;
                    discount = discountList.get(position).discount;
                }
                updateTotalPriceText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                discount = 0;
                updateTotalPriceText();
            }
        });
    }
}