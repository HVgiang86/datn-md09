package poly.manhnt.datn_md09.Presenters.address;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;

public interface AddressContract {
    interface ViewPayment {
        void onGetAddressDetailSuccess(UserAddress address);

        void onGetAddressFail(Exception e);
    }

    interface ViewAddress {
        void onGetAddressListFail(Exception e);

        void onGetAddressListSuccess(List<UserAddress> addressList);

        void onAddAddressSuccess();

        void onSetAddressSuccess(String addressId);
    }

    interface Presenter {

        void getAddressDetail(String addressId);

        void getListAddress(String uid);

        void setAddress(String uid, String addressId);

        void addAddress(String uid, String address, String addressDetail);
    }
}
