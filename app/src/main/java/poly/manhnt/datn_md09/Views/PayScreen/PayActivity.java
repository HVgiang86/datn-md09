package poly.manhnt.datn_md09.Views.PayScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.PayAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.DeliveryMethod.DeliveryMethod;
import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.Models.discount.UserDiscount;
import poly.manhnt.datn_md09.Presenters.UserDiscount.UserDiscountContract;
import poly.manhnt.datn_md09.Presenters.UserDiscount.UserDiscountPresenter;
import poly.manhnt.datn_md09.Presenters.address.AddressContract;
import poly.manhnt.datn_md09.Presenters.address.AddressPresenter;
import poly.manhnt.datn_md09.Presenters.cart.CartContract;
import poly.manhnt.datn_md09.Presenters.cart.CartPresenter;
import poly.manhnt.datn_md09.Presenters.payment.PaymentContract;
import poly.manhnt.datn_md09.Presenters.payment.PaymentPresenter;
import poly.manhnt.datn_md09.Views.AddressChoiceScreen.AddressChoiceActivity;
import poly.manhnt.datn_md09.Views.CartScreen.CartActivity;
import poly.manhnt.datn_md09.Views.DeliveryMethodScreen.DeliveryMethodActivity;
import poly.manhnt.datn_md09.databinding.ActivityPayBinding;
import poly.manhnt.datn_md09.utils.Utils;


public class PayActivity extends AppCompatActivity implements CartContract.PaymentView, PaymentContract.View, PayAdapter.OnItemClickListener, AddressContract.ViewPayment, UserDiscountContract.View {
    private final String[] paymentMethodString = {"Thanh toán online", "Thanh toán khi nhận hàng"};
    private final List<Cart> cartList = new ArrayList<>();
    PayAdapter payAdapter;
    private DeliveryMethod deliveryMethod = DataManager.getInstance().deliveryMethods.get(0);
    private int finalPrice = 0;
    private List<UserDiscount> discounts = new ArrayList<>();
    private String[] idCarts;
    private String discountId = "";
    private int amount = 0;
    private UserAddress address = null;
    private CartPresenter cartPresenter;
    private PaymentPresenter paymentPresenter;
    private AddressPresenter addressPresenter;
    private UserDiscountPresenter discountPresenter;
    private PaymentMethod paymentMethod = PaymentMethod.ONLINE;
    private ActivityPayBinding binding;
    private boolean hasAddress = false;
    private boolean hasIdCarts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(CartActivity.KEY_CARD_ID_ARRAY)) {
            idCarts = intent.getStringArrayExtra(CartActivity.KEY_CARD_ID_ARRAY);
            hasIdCarts = true;
        }
        if (intent.hasExtra(CartActivity.KEY_AMOUNT) && intent.hasExtra(CartActivity.KEY_DISCOUNT_ID)) {
            amount = intent.getIntExtra(CartActivity.KEY_AMOUNT, 0);
            discountId = intent.getStringExtra(CartActivity.KEY_DISCOUNT_ID);
        }

        cartPresenter = new CartPresenter(this);
        addressPresenter = new AddressPresenter(this);
        paymentPresenter = new PaymentPresenter(this);
        discountPresenter = new UserDiscountPresenter(this);

        initData();
        initPaymentMethodSpinner();
        updateDeliveryMethod();
        updateViewAddress();

        payAdapter = new PayAdapter(this);
        payAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerCart.setLayoutManager(layoutManager);
        binding.recyclerCart.setAdapter(payAdapter);

        binding.buttonOrder.setOnClickListener(v -> doPayment());

        binding.containerDeliveryMethod.setOnClickListener(v -> changeDeliveryMethod());

        binding.buttonChangeAddress.setOnClickListener(v -> changeAddress());

        initDiscountSpinner();
        calculateTotalPrice();
    }

    private void changeDeliveryMethod() {
        Intent intent = new Intent(this, DeliveryMethodActivity.class);
        intent.putExtra(DeliveryMethodActivity.KEY_DELIVERY_METHOD, deliveryMethod.id);
        startActivityForResult(intent, DeliveryMethodActivity.REQUEST_CODE);
    }

    private void changeAddress() {
        Intent intent = new Intent(this, AddressChoiceActivity.class);
        intent.putExtra(AddressChoiceActivity.KEY_CURRENT_ADDRESS_ID, address.id);
        startActivityForResult(intent, AddressChoiceActivity.REQUEST_CODE_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddressChoiceActivity.REQUEST_CODE_ADDRESS && resultCode == RESULT_OK) {
            String currentAddressId = data.getStringExtra(AddressChoiceActivity.KEY_CURRENT_ADDRESS_ID);
            if (currentAddressId != null && !currentAddressId.isEmpty()) {
                addressPresenter.getAddressDetail(currentAddressId);
                DataManager.getInstance().getUserLogin.address = currentAddressId;
                updateViewAddress();
            }
        } else if (requestCode == DeliveryMethodActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            int deliveryMethodId = data.getIntExtra(DeliveryMethodActivity.KEY_DELIVERY_METHOD, 0);
            for (DeliveryMethod method : DataManager.getInstance().deliveryMethods) {
                if (method.id == deliveryMethodId) {
                    deliveryMethod = method;
                }
            }

            updateDeliveryMethod();
            calculateTotalPrice();
        }


    }

    private void updateDeliveryMethod() {
        binding.textDeliveryName.setText(deliveryMethod.name);
        binding.textDeliveryTimeTaken.setText(deliveryMethod.timeTakenString);
        binding.textDeliveryFee.setText("đ" + deliveryMethod.fee);
    }

    private void initData() {
        cartPresenter.getCartList(DataManager.getInstance().getUserLogin.idUser);
        addressPresenter.getAddressDetail(DataManager.getInstance().getUserLogin.address);
        discountPresenter.getUserDiscount(DataManager.getInstance().getUserLogin.idUser);
    }

    private void doPayment() {
        if (paymentMethod == PaymentMethod.ONLINE)
            paymentPresenter.createOnlinePayment(DataManager.getInstance().getUserLogin.idUser, idCarts, amount, discountId);
        else
            paymentPresenter.createBill(DataManager.getInstance().getUserLogin.idUser, idCarts, amount, discountId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void openVpnWebView(String url) {
        System.out.println("URL: " + url);
        binding.webview.setVisibility(View.VISIBLE);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
    }

    private void calculateTotalPrice() {
        int totalOrigin = 0;
        for (Cart cart : cartList) {
            totalOrigin += cart.sizeColor.product.getFinalPrice() * cart.quantity;
        }

        System.out.println("Original price: " + totalOrigin);

        int discountAmount = 0;
        for (UserDiscount discount : discounts) {
            if (Utils.compare(discountId, discount.id)) {
                discountAmount = discount.discount;
                break;
            }
        }

        System.out.println("Discount amount: " + discountAmount);

        amount = totalOrigin <= discountAmount ? 0 : totalOrigin - discountAmount;

        System.out.println("Amount: " + amount);

        finalPrice = amount + deliveryMethod.fee;

        System.out.println("Final price: " + finalPrice);

        updateViewPrice();
    }

    private void updateViewPrice() {
        binding.textPrice.setText("đ" + amount);
        binding.textTotalPrice.setText("đ" + finalPrice);
        binding.textTotalPrice2.setText("đ" + finalPrice);
        binding.textShippingFee.setText("đ" + deliveryMethod.fee);
    }

    private void initPaymentMethodSpinner() {
        ArrayAdapter<String> adapterPaymentMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentMethodString);
        binding.paymentMethod.setAdapter(adapterPaymentMethod);
        binding.paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) paymentMethod = PaymentMethod.ONLINE;
                else if (position == 1) paymentMethod = PaymentMethod.COD;
                else paymentMethod = PaymentMethod.ONLINE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentMethod = PaymentMethod.ONLINE;
            }
        });
    }

    private void initDiscountSpinner() {

        List<String> discountNameList = new ArrayList<>();
        discountNameList.add("Không dùng");

        int selectedIndex = 0;
        for (UserDiscount discount : discounts) {
            discountNameList.add(discount.description);
            if (discountId != null && !discountId.isEmpty()) {
                if (Utils.compare(discountId, discount.id)) {
                    selectedIndex = discounts.indexOf(discount) + 1;
                }
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, discountNameList);
        binding.spinnerVoucher.setAdapter(arrayAdapter);
        binding.spinnerVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (discounts.size() != 0) {
                    discountId = discounts.get(position - 1).id;
                    calculateTotalPrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                discountId = "";
                calculateTotalPrice();
            }
        });

        binding.spinnerVoucher.setSelection(selectedIndex);
    }


    @Override
    public void onGetCartListSuccess(List<Cart> carts) {

        for (Cart cart : carts) {
            for (String idCart : idCarts) {
                if (Utils.compare(cart._id, idCart)) {
                    cartList.add(cart);
                }
            }
        }

        payAdapter.updateData(cartList);
        calculateTotalPrice();
    }

    @Override
    public void onGetCartFail(Exception e) {
        Toast.makeText(this, "Fail to load your cart", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemClickListener(String productId) {
        //TODO IMPLEMENT
    }

    @Override
    public void onCreateBillSuccess() {
        Toast.makeText(this, "Order successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateBillFail() {
        Toast.makeText(this, "Order fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOnlinePaymentSuccess(String url) {
        //TODO IMPLEMENT
        openVpnWebView(url);
    }

    @Override
    public void onCreateOnlinePaymentFail(Exception e) {
        Toast.makeText(this, "Fail create payment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAddressDetailSuccess(UserAddress address) {
        this.address = address;
        updateViewAddress();
    }


    private void updateViewAddress() {
        if (address == null) return;
        hasAddress = true;
        binding.textName.setText(DataManager.getInstance().getUserLogin.fullname);
        binding.textPhone.setText(DataManager.getInstance().getUserLogin.phone);
        binding.textAddress.setText(address.address + "\n" + address.addressDetail);
    }


    @Override
    public void onGetAddressFail(Exception e) {
        Toast.makeText(this, "Lấy địa chỉ thât bại! Vui lòng thiết lập địa chỉ giao hàng trước!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUserDiscountSuccess(List<UserDiscount> discountList) {
        discounts = discountList;
        System.out.println("On GetUserDiscount Succees");
        initDiscountSpinner();
    }

    enum PaymentMethod {
        ONLINE, COD
    }

}