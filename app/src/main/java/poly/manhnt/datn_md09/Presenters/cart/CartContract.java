package poly.manhnt.datn_md09.Presenters.cart;

import java.util.List;

import poly.manhnt.datn_md09.Models.cart.Cart;

public interface CartContract {
    interface View {
        void onGetCartListSuccess(List<Cart> carts);

        void onGetCartFail(Exception e);

        void onUpdateCartItemSuccess(String cartId, int quantity);

        void onUpdateCartFail(Exception e);
    }

    interface PaymentView{
        void onGetCartListSuccess(List<Cart> carts);

        void onGetCartFail(Exception e);
    }

    interface Presenter {
        void getCartList(String uid);

        void updateCartItem(String uid, String cartId, int quantity);

        void deleteCartItem(String cartId);

    }
}
