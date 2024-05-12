package poly.manhnt.datn_md09.Views.UpdateProfile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import poly.manhnt.datn_md09.Const.AppConfig;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Presenters.account.AccountContract;
import poly.manhnt.datn_md09.Presenters.account.AccountPresenter;
import poly.manhnt.datn_md09.databinding.ActivityUpdateProfileBinding;
import poly.manhnt.datn_md09.utils.FileUtils;

public class UpdateProfileActivity extends AppCompatActivity implements AccountContract.View {
    private ActivityUpdateProfileBinding binding;
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;
    private AccountPresenter presenter;
    private Uri tempAvatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new AccountPresenter(this);

        binding.buttonSave.setOnClickListener(v -> handleChangePassword());
        binding.buttonChangeAvt.setOnClickListener(v -> chooseFileFromStorage());
        binding.textName.setText(DataManager.getInstance().getUserLogin.fullname);
        binding.textPhone.setText(DataManager.getInstance().getUserLogin.phone);
        binding.textAddress.setText(DataManager.getInstance().getUserLogin.address_city);
        String url = AppConfig.API_URL + DataManager.getInstance().getUserLogin.avata;
        Glide.with(this).load(url).centerCrop().into(binding.imageAvatar);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void handleChangePassword() {
        String oldPassword = binding.edtOldPassword.getText().toString().trim();
        String newPassword = binding.edtNewPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            binding.edtOldPassword.setError("Please enter your old password");
            binding.edtOldPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            binding.edtNewPassword.setError("Please enter your new password");
            binding.edtNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmPassword.setError("Please confirm your password");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError("Password does not match");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        if (newPassword.equals(oldPassword)) {
            binding.edtNewPassword.setError("New password cannot be the same as old password");
            binding.edtNewPassword.requestFocus();
            return;
        }

        presenter.changePassword(DataManager.getInstance().getUserLogin.idUser, oldPassword, newPassword);
    }

    @Override
    public void onChangePasswordSuccess() {
        Toast.makeText(this, "Change password success", Toast.LENGTH_SHORT).show();
        binding.edtOldPassword.setText("");
        binding.edtNewPassword.setText("");
        binding.edtConfirmPassword.setText("");

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onChangePasswordFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangeAvatarSuccess() {
        File file = getFileFromUri(tempAvatarUri);
        Glide.with(this).load(file).centerCrop().into(binding.imageAvatar);
        Toast.makeText(this, "Change avatar success", Toast.LENGTH_SHORT).show();

    }

    private void chooseFileFromStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Requesting permission");
            // Permission not granted, request it
            String[] permissions = new String[]{
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
            };
            ActivityCompat.requestPermissions(this, permissions, 2);
        } else {
            // Permission already granted, proceed with accessing file
            // (use your method to parse the file path from the URI)
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Request code: " + requestCode);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                System.out.println(uri);

                //Check file size
                if (!checkFileSize(uri)) {
                    Toast.makeText(this, "File size exceeds the limit of " + MAX_FILE_SIZE + " bytes.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // do something with the uri
                handleImageSelected(uri);
            }
        }
    }

    private void handleImageSelected(Uri uri) {
        tempAvatarUri = uri;
        File file = getFileFromUri(uri);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("img-avata", file.getName(), requestBody);

        presenter.changeAvatar(DataManager.getInstance().getUserLogin.idUser, imagePart);
    }

    private File getFileFromUri(Uri uri) {
        // Do something with the selected image
        System.out.println("Selected image: " + uri.getPath());
        String path;
        path = FileUtils.getPathFromUri(this, uri);
        String filePath = path == null ? "" : path;

        System.out.println("File path: " + filePath);
        File file = new File(filePath);
        return file;
    }

    private boolean checkFileSize(Uri uri) {
        // Get the file size
        long fileSize = getFileSize(uri);
        if (fileSize == -1) return false;

        // Check if the file size is within the limit
        return fileSize <= MAX_FILE_SIZE;
    }

    private long getFileSize(Uri uri) {
        try {
            // Get the file's content resolver
            android.content.ContentResolver resolver = getContentResolver();
            // Query to get the file's size
            android.database.Cursor cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                // Get the file size
                @SuppressLint("Range") long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                cursor.close();
                return size;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}