package poly.manhnt.datn_md09.Views.RegisterScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import poly.manhnt.datn_md09.Models.model_register.RegisterRequest;
import poly.manhnt.datn_md09.Models.model_register.RegisterResponse;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edtUsername, edtEmail, edtFullName, edtPhone, edtPasswordRegister, edtConfirmPassword;
    private TextView tvRegister, tvSignIn;

    private String typeSuccess = "Username đã được đăng ký";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initEvents();
    }

    private void initEvents() {
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = Objects.requireNonNull(edtUsername.getText()).toString().trim();
        String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
        String fullName = Objects.requireNonNull(edtFullName.getText()).toString().trim();
        String phoneNumber = Objects.requireNonNull(edtPhone.getText()).toString().trim();
        String password = Objects.requireNonNull(edtPasswordRegister.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(edtConfirmPassword.getText()).toString().trim();

        if(password.equals(confirmPassword)) {
            RetrofitClient.getInstance().create(ApiService.class).registerUser(new RegisterRequest(username, password,email, fullName, phoneNumber)).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if(response.body().msg.equals(typeSuccess)) {
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "Xác thực mật khẩu bị sai!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tvRegister = findViewById(R.id.tvRegister);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

}