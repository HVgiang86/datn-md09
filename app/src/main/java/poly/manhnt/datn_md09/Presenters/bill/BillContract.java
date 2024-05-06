package poly.manhnt.datn_md09.Presenters.bill;

import java.util.List;

import okhttp3.MultipartBody;
import poly.manhnt.datn_md09.Models.Bill.Bill;

public interface BillContract {
    interface View {
        void onGetBillListSuccess(List<Bill> billList);

        void onGetBillListError(Exception e);

        void onUpdateBillStatusSuccess(String billId, int status);

        void onUploadCommentSuccess();

    }

    interface Presenter {
        void getBillList(String uid);

        void updateBillStatus(String billId, int status);

        void uploadComment(String uid, String productId, String sizeColorId, String comment, String rating, List<MultipartBody.Part> images);

    }
}
