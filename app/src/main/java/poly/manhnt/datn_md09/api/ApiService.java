package poly.manhnt.datn_md09.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import poly.manhnt.datn_md09.Models.Account.ChangePasswordRequest;
import poly.manhnt.datn_md09.Models.Bill.BillDetailResponse;
import poly.manhnt.datn_md09.Models.Bill.BillResponse;
import poly.manhnt.datn_md09.Models.Bill.BillStatusRequest;
import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.MessageResponse;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.ProductComment.ProductRatingRequest;
import poly.manhnt.datn_md09.Models.ProductComment.ProductRatingResponse;
import poly.manhnt.datn_md09.Models.ProductDetail.ProductDetailResponse;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Models.ProductSearch.ProductSearchResponse;
import poly.manhnt.datn_md09.Models.ProductSizeColor.ProductSizeColorResponse;
import poly.manhnt.datn_md09.Models.UserAddress.AddAddressRequest;
import poly.manhnt.datn_md09.Models.UserAddress.AddressResponse;
import poly.manhnt.datn_md09.Models.UserAddress.SetAddressRequest;
import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;
import poly.manhnt.datn_md09.Models.cart.CartRequest;
import poly.manhnt.datn_md09.Models.cart.CartResponse;
import poly.manhnt.datn_md09.Models.discount.DiscountResponse;
import poly.manhnt.datn_md09.Models.model_login.LoginRequest;
import poly.manhnt.datn_md09.Models.model_login.LoginResponse;
import poly.manhnt.datn_md09.Models.model_register.RegisterRequest;
import poly.manhnt.datn_md09.Models.model_register.RegisterResponse;
import poly.manhnt.datn_md09.Models.payment.PaymentRequest;
import poly.manhnt.datn_md09.Models.payment.VnpResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Call<MessageResponse> addCart(@Path("uid") String uid, @Path("size_color_id") String sizeColorId, @Body CartRequest cartRequest);

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

    @POST("order/create_payment_url/{uid}")
    Call<VnpResponse> onlinePayment(@Path("uid") String uid, @Body PaymentRequest request);

    @GET("get-address/{addressId}")
    Call<AddressResponse> getAddressDetail(@Path("addressId") String addressId);

    @GET("address/{uid}")
    Call<List<UserAddress>> getAddressList(@Path("uid") String uid);

    @POST("setaddress")
    Call<Void> setAddress(@Body SetAddressRequest request);

    @POST("address")
    Call<Void> addAddress(@Body AddAddressRequest request);

    @POST("addbill/{uid}")
    Call<MessageResponse> addBill(@Path("uid") String uid, @Body PaymentRequest request);

    @GET("bill/{uid}")
    Call<BillResponse> getBillList(@Path("uid") String uid);

    @GET("bill-by-id/{billId}")
    Call<BillDetailResponse> getBillDetail(@Path("billId") String billId);

    @DELETE("bill/{billId}")
    Call<Void> deleteBill(@Path("billId") String billId);

    @PUT("bill/{billId}")
    Call<Void> updateBillStatus(@Path("billId") String billId, @Body BillStatusRequest request);

    @Multipart
    @POST("comment")
    Call<Void> addComment(@Part List<MultipartBody.Part> images, @Part("UserId") RequestBody userId, @Part("ProductId") RequestBody productId, @Part("ProductDetailId") RequestBody productDetailId, @Part("Comment") RequestBody comment, @Part("rating") RequestBody rating);

    @POST("comment-by-id")
    Call<ProductRatingResponse> getCommentById(@Body ProductRatingRequest request);

    @PUT("change-password/{uid}")
    Call<MessageResponse> changePassword(@Path("uid") String uid, @Body ChangePasswordRequest request);

    @Multipart
    @POST("users/{uid}")
    Call<Void> changeAvatar(@Part MultipartBody.Part images, @Path("uid") String uid);
}
