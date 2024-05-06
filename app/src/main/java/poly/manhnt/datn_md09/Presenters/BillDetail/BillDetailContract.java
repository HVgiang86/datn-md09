package poly.manhnt.datn_md09.Presenters.BillDetail;

import java.util.List;

import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.Models.cart.Cart;

public interface BillDetailContract {
    interface View {
        void onGetBillDetailSuccess(Bill bill);

        void onGetBillDetailError(String error);
    }

    interface Presenter {
        void getBillDetail(String billId);
    }
}
