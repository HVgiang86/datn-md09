package poly.manhnt.datn_md09.Presenters.UserDiscount;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import poly.manhnt.datn_md09.Models.discount.DiscountResponse;
import poly.manhnt.datn_md09.Models.discount.UserDiscount;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDiscountPresenter implements UserDiscountContract.Presenter {

    private final UserDiscountContract.View view;

    public UserDiscountPresenter(UserDiscountContract.View view) {
        this.view = view;
    }

    @Override
    public void getUserDiscount(String uid) {
        try {
            RetrofitClient.getInstance().create(ApiService.class).getUserDiscount(uid).enqueue(new Callback<DiscountResponse>() {
                @Override
                public void onResponse(Call<DiscountResponse> call, Response<DiscountResponse> response) {
                    if (response.isSuccessful()) {
                        List<UserDiscount> discountList = new ArrayList<>();
                        response.body().discountList.forEach(userDiscount -> {
                            String endDate = userDiscount.endDateString;
                            if (isValidEndDate(endDate)) {
                                discountList.add(userDiscount);
                            }
                        });

                        if (!discountList.isEmpty()) {
                            view.onGetUserDiscountSuccess(discountList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<DiscountResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEndDate(String endDate) {
        try {
            // Define the formatter for the given format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Parse the string to LocalDateTime object
            Date parsedDate = formatter.parse(endDate);
            Date currentDate = new Date();

            if (parsedDate != null && parsedDate.before(currentDate)) {
                return false;
            } else return parsedDate != null && parsedDate.after(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
