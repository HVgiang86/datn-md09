package poly.manhnt.datn_md09.Views.AddressChoiceScreen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import poly.manhnt.datn_md09.Adapters.AddressAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.Presenters.address.AddressContract;
import poly.manhnt.datn_md09.Presenters.address.AddressPresenter;
import poly.manhnt.datn_md09.databinding.ActivityAddressChoiceBinding;
import poly.manhnt.datn_md09.databinding.NewAddressLayoutBinding;

public class AddressChoiceActivity extends AppCompatActivity implements AddressContract.ViewAddress, AddressAdapter.OnItemClickListener {
    public static final String KEY_CURRENT_ADDRESS_ID = "CurrEnt_Address_Id";
    public static final int REQUEST_CODE_ADDRESS = 111;
    private ActivityAddressChoiceBinding binding;
    private AddressPresenter presenter;
    private AddressAdapter adapter;
    private String currentAddressId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressChoiceBinding.inflate(getLayoutInflater());

        currentAddressId = getIntent().getStringExtra(KEY_CURRENT_ADDRESS_ID);
        if (currentAddressId == null) currentAddressId = "";

        setContentView(binding.getRoot());
        presenter = new AddressPresenter(this);

        adapter = new AddressAdapter(this);
        adapter.setListener(this);
        binding.recycler.setAdapter(adapter);

        binding.toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        binding.buttonAddAddress.setOnClickListener(v -> {
            openNewAddressPopup();
        });

        initData();
    }

    private void openNewAddressPopup() {
        Dialog dialog = new Dialog(this);
        NewAddressLayoutBinding dialogBinding = NewAddressLayoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.buttonSubmit.setOnClickListener(v -> {
            String commune = dialogBinding.inputCommune.getText().toString();
            String district = dialogBinding.inputDistrict.getText().toString();
            String province = dialogBinding.inputProvince.getText().toString();

            String address = " " + commune + ", " + district + ", " + province;
            String addressDetail = " " + dialogBinding.inputStreet.getText().toString();

            presenter.addAddress(DataManager.getInstance().getUserLogin.idUser, address, addressDetail);
            dialog.dismiss();

        });

        dialog.show();
    }

    private void initData() {
        presenter.getListAddress(DataManager.getInstance().getUserLogin.idUser);
    }

    @Override
    public void onGetAddressListFail(Exception e) {
        Toast.makeText(this, "get list fail", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onGetAddressListSuccess(List<UserAddress> addressList) {
        adapter.updateData(addressList);
        adapter.initCurrentAddressId(currentAddressId);
    }

    @Override
    public void onAddAddressSuccess() {
        Toast.makeText(this, "Add address success fully", Toast.LENGTH_SHORT).show();
        presenter.getListAddress(DataManager.getInstance().getUserLogin.idUser);
    }

    @Override
    public void onSetAddressSuccess(String addressId) {
        Intent intent = new Intent();
        intent.putExtra(KEY_CURRENT_ADDRESS_ID, currentAddressId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(String addressId) {
        this.currentAddressId = addressId;
        System.out.println("Current id: " + addressId);
        presenter.setAddress(DataManager.getInstance().getUserLogin.idUser, addressId);
    }


}