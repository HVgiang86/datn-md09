package poly.manhnt.datn_md09.Presenters.address;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserAddress.AddAddressRequest;
import poly.manhnt.datn_md09.Models.UserAddress.AddressResponse;
import poly.manhnt.datn_md09.Models.UserAddress.SetAddressRequest;
import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressPresenter implements AddressContract.Presenter {
    private AddressContract.ViewPayment viewPayment;
    private AddressContract.ViewAddress viewAddress;

    public AddressPresenter(AddressContract.ViewPayment viewPayment) {
        this.viewPayment = viewPayment;
    }

    public AddressPresenter(AddressContract.ViewAddress viewAddress) {
        this.viewAddress = viewAddress;
    }

    @Override
    public void getAddressDetail(String addressId) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getAddressDetail(addressId).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            viewPayment.onGetAddressDetailSuccess(response.body().address);
                        }
                    } else {
                        viewPayment.onGetAddressFail(new Exception("Fail to get address detail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    t.printStackTrace();
                    viewPayment.onGetAddressFail(new Exception("Fail to get address detail: " + t.getMessage()));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            viewPayment.onGetAddressFail(e);
        }
    }

    @Override
    public void getListAddress(String uid) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getAddressList(uid).enqueue(new Callback<List<UserAddress>>() {
                @Override
                public void onResponse(Call<List<UserAddress>> call, Response<List<UserAddress>> response) {
                    if (response.isSuccessful()) {
                        viewAddress.onGetAddressListSuccess(response.body());
                    } else {
                        viewAddress.onGetAddressListFail(new Exception("Get fail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<List<UserAddress>> call, Throwable t) {
                    t.printStackTrace();
                    viewAddress.onGetAddressListFail(new Exception("Get fail: " + t.getMessage()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            viewAddress.onGetAddressListFail(e);
        }
    }

    @Override
    public void setAddress(String uid, String addressId) {
        try {
            SetAddressRequest request = new SetAddressRequest(uid, addressId);
            RetrofitClient.getInstance().create(ApiService.class).setAddress(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        viewAddress.onSetAddressSuccess(addressId);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAddress(String uid, String address, String addressDetail) {
        try {
            AddAddressRequest request = new AddAddressRequest(uid, address, addressDetail);
            RetrofitClient.getInstance().create(ApiService.class).addAddress(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        viewAddress.onAddAddressSuccess();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
