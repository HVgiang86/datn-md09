package poly.manhnt.datn_md09.Presenters.cart;

import poly.manhnt.datn_md09.Models.cart.CartRequest;
import poly.manhnt.datn_md09.Models.cart.CartResponse;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view = null;
    private CartContract.PaymentView paymentView = null;

    public CartPresenter(CartContract.View view) {
        this.view = view;
    }

    public CartPresenter(CartContract.PaymentView paymentView) {
        this.paymentView = paymentView;
    }

    @Override
    public void getCartList(String uid) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getCartList(uid).enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful()) {
                        if (view != null) view.onGetCartListSuccess(response.body().cartList);
                        if (paymentView != null)
                            paymentView.onGetCartListSuccess(response.body().cartList);
                    } else {
                        if (view != null)
                            view.onGetCartFail(new Exception("Fail: " + response.message()));
                        if (paymentView != null)
                            paymentView.onGetCartFail(new Exception("Fail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (view != null) view.onGetCartFail(new Exception("Fail: " + t.getMessage()));
                    if (paymentView != null)
                        paymentView.onGetCartFail(new Exception("Fail: " + t.getMessage()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) view.onGetCartFail(e);
            if (paymentView != null) paymentView.onGetCartFail(e);
        }
    }

    @Override
    public void updateCartItem(String uid, String cartId, int quantity) {
        try {
            CartRequest cartRequest = new CartRequest(quantity);
            RetrofitClient.getInstance().create(ApiService.class).updateCartItem(uid, cartId, cartRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        view.onUpdateCartItemSuccess(cartId, quantity);
                    } else {
                        view.onUpdateCartFail(new Exception("Fail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    view.onUpdateCartFail(new Exception("Fail: " + t.getMessage()));
                }
            });
        } catch (Exception e) {
            view.onUpdateCartFail(e);
        }
    }

    @Override
    public void deleteCartItem(String cartId) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).deleteCartItem(cartId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        view.onUpdateCartItemSuccess(cartId, 0);
                    } else {
                        view.onUpdateCartFail(new Exception("Fail: " + response.message()));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    view.onUpdateCartFail(new Exception("Fail: " + t.getMessage()));
                }
            });
        } catch (Exception e) {
            view.onUpdateCartFail(e);
        }
    }
}
