package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserAddress.UserAddress;
import poly.manhnt.datn_md09.databinding.ItemAddressBinding;
import poly.manhnt.datn_md09.utils.Utils;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private final Context context;
    private List<UserAddress> addressList = null;
    private String currentAddressId = "";

    private OnItemClickListener listener = null;

    public AddressAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<UserAddress> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void initCurrentAddressId(String currentAddressId) {
        String oldAddressId = currentAddressId;
        this.currentAddressId = currentAddressId;
        int oldPosition = 0;
        int currentPosition = 0;

        for (UserAddress address : addressList) {
            int index = addressList.indexOf(address);

            if (Utils.compare(address.id, oldAddressId)) {
                oldPosition = index;
            }

            if (Utils.compare(address.id, currentAddressId)) {
                currentPosition = index;
            }
        }

        notifyItemChanged(oldPosition);
        notifyItemChanged(currentPosition);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemAddressBinding binding = ItemAddressBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAddressBinding binding = holder.binding;
        UserAddress address = addressList.get(position);
        binding.textAddress.setText(address.address + "\n" + address.addressDetail);

        if (currentAddressId.isEmpty()) {
            binding.textDefaultAddress.setVisibility(View.GONE);
        } else {
            if (Utils.compare(address.id, currentAddressId)) {
                binding.textDefaultAddress.setVisibility(View.VISIBLE);
            } else {
                binding.textDefaultAddress.setVisibility(View.GONE);
            }
        }

//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) listener.onClick(address.id);
//        });

        holder.binding.buttonChangeAddress.setOnClickListener(v -> {
            System.out.println("CLICk");
            if (listener != null) listener.onClick(address.id);
        });
    }

    @Override
    public int getItemCount() {
        return (addressList == null) ? 0 : addressList.size();
    }

    public interface OnItemClickListener {
        void onClick(String addressId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAddressBinding binding;

        public ViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
