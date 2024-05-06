package poly.manhnt.datn_md09.Views.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import poly.manhnt.datn_md09.R;

public class OrderFailFragment extends DialogFragment {
    private final OnButtonAcceptClickListener listener;

    public OrderFailFragment(OnButtonAcceptClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        if (context != null) {
            Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_popup_order_fail);
            dialog.setCancelable(false);

            dialog.show();

            dialog.findViewById(R.id.btn_accept).setOnClickListener(v -> {
                dialog.dismiss();
                listener.onOrderFailButtonAcceptClick();
            });

            return dialog;
        } else return super.onCreateDialog(savedInstanceState);
    }

    public interface OnButtonAcceptClickListener {
        void onOrderFailButtonAcceptClick();
    }
}
