package poly.manhnt.datn_md09.Views.BillDetail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import poly.manhnt.datn_md09.Adapters.PayAdapter;
import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.Presenters.BillDetail.BillDetailContract;
import poly.manhnt.datn_md09.Presenters.BillDetail.BillDetailPresenter;
import poly.manhnt.datn_md09.Presenters.address.AddressContract;
import poly.manhnt.datn_md09.Presenters.address.AddressPresenter;
import poly.manhnt.datn_md09.databinding.ActivityBillDetailBinding;

public class BillDetailActivity extends AppCompatActivity implements BillDetailContract.View, AddressContract.ViewPayment {

    public static final String KEY_BILL_ID = "key_bill_id";
    private ActivityBillDetailBinding binding;
    private BillDetailContract.Presenter presenter;
    private AddressPresenter addressPresenter;
    private String billId;
    private PayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new BillDetailPresenter(this);
        addressPresenter = new AddressPresenter(this);

        billId = getIntent().getStringExtra(KEY_BILL_ID);
        billId = billId == null ? "" : billId;

        if (billId.isEmpty()) {
            finish();
            return;
        }

        adapter = new PayAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerBillCart.setLayoutManager(layoutManager);
        binding.recyclerBillCart.setAdapter(adapter);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        initData();
    }

    private void initData() {
        presenter.getBillDetail(billId);
    }

    @Override
    public void onGetBillDetailSuccess(Bill bill) {
        adapter.updateData(bill.carts);
        binding.textName.setText(bill.user.name);
        binding.textPhone.setText(bill.user.phone);

        int totalPrice = 0;
        for (Cart cart : bill.carts) {
            int price = cart.sizeColor.product.getFinalPrice();
            int quantity = cart.quantity;
            totalPrice += price * quantity;
        }

        int discount = 0;
        if (bill.discount != null) discount = bill.discount.discount;

        int totalAmount = bill.totalAmount;
        int deliveryFee = totalAmount - discount - totalPrice;

        binding.textPrice.setText("đ" + totalPrice);
        binding.textDiscount.setText("đ" + discount);
        binding.textShippingFee.setText("đ" + deliveryFee);
        binding.textTotalPrice.setText("đ" + totalAmount);
        addressPresenter.getAddressDetail(bill.user.addressId);
    }


    @Override
    public void onGetBillDetailError(String error) {
        finish();
    }

    @Override
    public void onGetAddressDetailSuccess(UserAddress address) {
        binding.textAddress.setText(address.addressDetail);
    }

    @Override
    public void onGetAddressFail(Exception e) {

    }
}