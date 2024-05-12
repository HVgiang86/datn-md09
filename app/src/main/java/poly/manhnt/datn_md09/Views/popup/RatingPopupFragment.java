package poly.manhnt.datn_md09.Views.popup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.utils.FileUtils;

public class RatingPopupFragment extends DialogFragment {
    private static final int MAX_IMAGES_ALLOWED = 3; // Set the maximum number of images allowed
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB maximum file size

    private final OnButtonAcceptClickListener listener;
    private final Bill bill;
    private final List<Uri> selectedImages = new ArrayList<>();
    private final List<MultipartBody.Part> imageParts = new ArrayList<>();
    private String selectedProductId;
    private String selectedSizeColorId;

    public RatingPopupFragment(OnButtonAcceptClickListener listener, Bill bill) {
        this.listener = listener;
        this.bill = bill;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        if (context != null) {
            Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.layout_popup_rating);
            dialog.setCancelable(false);

            dialog.show();

            Button btnAccept = dialog.findViewById(R.id.btn_accept);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            TextView textRatingNumber = dialog.findViewById(R.id.text_rating_number);
            EditText edtComment = dialog.findViewById(R.id.edt_comment);
            RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
            TextView buttonImageUpload = dialog.findViewById(R.id.button_image_upload);
            Spinner productSpinner = dialog.findViewById(R.id.spinner);

            List<String> listProduct = new ArrayList<>();
            for (Cart cart : bill.carts) {
                String name = cart.sizeColor.product.name;
                listProduct.add(name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listProduct);
            productSpinner.setAdapter(adapter);
            productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedProductId = bill.carts.get(position).sizeColor.product._id;
                    selectedSizeColorId = bill.carts.get(position).sizeColor.sizeColorId;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedProductId = null;
                    selectedSizeColorId = null;
                }
            });

            btnAccept.setOnClickListener(v -> {
                dialog.dismiss();
                String rating = textRatingNumber.getText().toString();
                String comment = edtComment.getText().toString().trim();
                System.out.println("Comment: " + comment);

                if (selectedProductId != null && selectedSizeColorId != null)
                    listener.onOrderSuccessButtonAcceptClick(imageParts, rating, comment, selectedProductId, selectedSizeColorId);
            });

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            ratingBar.setOnRatingBarChangeListener((rating_bar, rating, fromUser) -> {
                textRatingNumber.setText(String.valueOf(rating));
            });

            buttonImageUpload.setOnClickListener(v -> chooseFileFromStorage());

            return dialog;
        } else return super.onCreateDialog(savedInstanceState);
    }


    private void chooseFileFromStorage() {
        try {
            if (getContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                gotoPermissionSettings();
            } else {
                selectedImages.clear();

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    if (selectedImages.size() < MAX_IMAGES_ALLOWED) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();

                        //Check file size
                        if (!checkFileSize(imageUri)) {
                            showToast("File size exceeds the limit of " + MAX_FILE_SIZE + " bytes.");
                            return;
                        }

                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        selectedImages.add(uri);
                    } else {
                        showToast("You can only select up to " + MAX_IMAGES_ALLOWED + " images.");
                    }
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();

                //Check file size
                if (!checkFileSize(uri)) {
                    showToast("File size exceeds the limit of " + MAX_FILE_SIZE + " bytes.");
                    return;
                }
                // do something with the uri
                selectedImages.add(uri);
            }
        }

        if (!selectedImages.isEmpty()) handleImageSelected();
    }

    private void handleImageSelected() {
        imageParts.clear();

        // Handle the selected images here
        for (Uri uri : selectedImages) {
            // Do something with the selected image
            System.out.println("Selected image: " + uri.getPath());
            String path;
            path = FileUtils.getPathFromUri(requireContext(), uri);
            String filePath = path == null ? "" : path;

            System.out.println("File path: " + filePath);
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            imageParts.add(imagePart);
        }
    }

    private void showToast(String message) {
        // Display a toast message
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
            android.content.ContentResolver resolver = requireContext().getContentResolver();
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


    private void gotoPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);

        intent.setData(uri);
        startActivity(intent);
    }

    public interface OnButtonAcceptClickListener {
        void onOrderSuccessButtonAcceptClick(List<MultipartBody.Part> imageParts, String rating, String comment, String productId, String productSizeColor);
    }
}
