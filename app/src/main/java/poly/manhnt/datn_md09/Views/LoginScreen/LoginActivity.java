package poly.manhnt.datn_md09.Views.LoginScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.model_login.LoginRequest;
import poly.manhnt.datn_md09.Models.model_login.LoginResponse;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.Views.HomeScreen.HomeActivity;
import poly.manhnt.datn_md09.Views.RegisterScreen.RegisterActivity;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView tvLogin, tvSignUp;
    private TextInputEditText edtUsername, edtPassword;

    private String typeSuccess = "Đăng nhập thành công";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initEvents();
    }

    private void initViews() {
        tvLogin = findViewById(R.id.tvLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
    }

    private void initEvents() {
        tvLogin.setOnClickListener(view -> loginUser());

        tvSignUp.setOnClickListener(view -> navigateRegisterScreen());
    }

    private void loginUser() {

        String username = Objects.requireNonNull(edtUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

        RetrofitClient.getInstance().create(ApiService.class).loginUser(new LoginRequest(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().msg.equals(typeSuccess)) {
                    DataManager.getInstance().getUserLogin = response.body();
                    System.out.println("Login success: " + DataManager.getInstance().getUserLogin.idUser);
                    navigateHomeScreen();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void navigateRegisterScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}