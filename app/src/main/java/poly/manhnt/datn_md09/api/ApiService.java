package poly.manhnt.datn_md09.api;

import java.util.List;

import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.MessageResponse;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductDetail.ProductDetailResponse;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Models.ProductSearch.ProductSearchResponse;
import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductSizeColorResponse;
import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;
import poly.manhnt.datn_md09.Models.cart.CartRequest;
import poly.manhnt.datn_md09.Models.cart.CartResponse;
import poly.manhnt.datn_md09.Models.discount.DiscountResponse;
import poly.manhnt.datn_md09.Models.model_login.LoginRequest;
import poly.manhnt.datn_md09.Models.model_login.LoginResponse;
import poly.manhnt.datn_md09.Models.model_register.RegisterRequest;
import poly.manhnt.datn_md09.Models.model_register.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("userslogin")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET("products/{page}")
    Call<List<ProductResponse>> getListProduct(@Path("page") int page);

    @GET("product-by-id/{id}")
    Call<ProductDetailResponse> getProductDetail(@Path("id") String productId);

    @GET("comment/{id}")
    Call<List<ProductComment>> getProductComment(@Path("id") String productId);

    @GET("getListAll_deltail/{id}")
    Call<ProductSizeColorResponse> getProductSizeColor(@Path("id") String productId);

    @POST("addCart/{uid}/{size_color_id}")
    Call<MessageResponse> addCart(@Path("uid") String uid, @Path("size_color_id") String sizeColorId);

    @GET("categorys")
    Call<CategoryIdResponse> getCategories();

    @GET("products")
    Call<ProductSearchResponse> searchByName(@Query("searchValues") String searchValue);

    @GET("notification/{uid}")
    Call<List<UserNotification>> getNotification(@Path("uid") String uid);

    @GET("notification-read/{notificationId}")
    Call<Void> markReadNotification(@Path("notificationId") String notificationId);

    @GET("getListCart/{uid}")
    Call<CartResponse> getCartList(@Path("uid") String uid);

    @POST("updateCart/{uid}/{cartId}")
    Call<Void> updateCartItem(@Path("uid") String uid, @Path("cartId") String cartId, @Body CartRequest cartRequest);

    @DELETE("deletecart/{cartId}")
    Call<Void> deleteCartItem(@Path("cartId") String cartId);

    @GET("discount/{uid}")
    Call<DiscountResponse> getUserDiscount(@Path("uid") String uid);

}
