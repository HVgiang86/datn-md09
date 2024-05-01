package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import poly.manhnt.datn_md09.Models.DeliveryMethod.DeliveryMethod;
import poly.manhnt.datn_md09.databinding.ItemDeliveryMethodBinding;

public class DeliveryMethodAdapter extends RecyclerView.Adapter<DeliveryMethodAdapter.ViewHolder> {
    private final List<DeliveryMethod> deliveryMethodList;
    private final Context context;
    private OnItemClickListener listener = null;
    private int currentMethodId = 0;

    public DeliveryMethodAdapter(List<DeliveryMethod> deliveryMethodList, Context context, int currentMethodId) {
        this.deliveryMethodList = deliveryMethodList;
        this.context = context;
        this.currentMethodId = currentMethodId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemDeliveryMethodBinding binding = ItemDeliveryMethodBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDeliveryMethodBinding binding = holder.binding;
        DeliveryMethod method = deliveryMethodList.get(position);

        binding.textName.setText(method.name);
        binding.textDeliveryTimeTaken.setText(method.timeTakenString);
        binding.textFee.setText("đ" + method.fee);

        if (method.id == currentMethodId) {
            binding.textDefaultAddress.setVisibility(View.VISIBLE);
        } else {
            binding.textDefaultAddress.setVisibility(View.GONE);
        }

        binding.buttonChangeMethod.setOnClickListener(v -> {

            if (listener != null) listener.onClick(method.id);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryMethodList.size();
    }

    public interface OnItemClickListener {
        void onClick(int methodId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDeliveryMethodBinding binding;

        public ViewHolder(@NonNull ItemDeliveryMethodBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
