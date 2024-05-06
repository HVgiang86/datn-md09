package poly.manhnt.datn_md09.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.databinding.ItemPaymentCartBinding;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {
    Context context;
    List<Cart> carts;

    private OnItemClickListener listener;

    public PayAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<Cart> carts) {
        this.carts = carts;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemPaymentCartBinding binding = ItemPaymentCartBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemPaymentCartBinding binding = holder.binding;
        Cart cart = carts.get(position);
        binding.textName.setText(cart.sizeColor.product.name);
        binding.textPrice.setText("" + cart.sizeColor.product.getFinalPrice());
        binding.textQuantity.setText("x" + cart.quantity);
        String color = cart.sizeColor.color.colorName;
        String size = cart.sizeColor.size.sizeName;
        binding.textSizeColor.setText(size + ", " + color);

        Glide.with(context).load(cart.sizeColor.product.image.get(0)).centerCrop().into(binding.imageProduct);

        holder.itemView.setOnClickListener(v -> listener.onItemClickListener(cart.sizeColor.product._id));
    }

    @Override
    public int getItemCount() {
        return carts == null ? 0 : carts.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(String productId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPaymentCartBinding binding;
        CardView cardView;

        public ViewHolder(@NonNull ItemPaymentCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

