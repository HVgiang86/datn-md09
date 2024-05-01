package poly.manhnt.datn_md09.Presenters.payment;

public interface PaymentContract {
    interface View {
        void onCreateBillSuccess();

        void onCreateBillFail();

        void onCreateOnlinePaymentSuccess(String url);

        void onCreateOnlinePaymentFail(Exception e);
    }

    interface Presenter {
        void createOnlinePayment(String uid, String[] cartIds, int amount, String discountId);

        void createBill(String uid, String[] cartIds, int amount, String discountId);
    }
}
