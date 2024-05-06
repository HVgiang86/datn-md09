package poly.manhnt.datn_md09.Views.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import poly.manhnt.datn_md09.R;

public class LoadingPopupFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        if (context != null) {
            Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_loading);
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } else return super.onCreateDialog(savedInstanceState);
    }
}
