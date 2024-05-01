package poly.manhnt.datn_md09.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Models.cart.Cart;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.ItemCartBinding;
import poly.manhnt.datn_md09.utils.Utils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<MyPair> carts = new ArrayList<>();
    private OnItemInteractListener listener;

    public void setOnItemInteractListener(OnItemInteractListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Cart> carts) {
        System.out.println("update cart adapter");
        for (Cart c : carts) {
            this.carts.add(new MyPair(c, false));
        }

        notifyDataSetChanged();
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        int totalPrice = 0;

        for (MyPair pair : carts) {
            Cart cart = pair.first;
            boolean isCheck = pair.second;

            int finalPrice = 0;
            if (cart.sizeColor.product.discount == null) finalPrice = cart.sizeColor.product.price;
            else finalPrice = cart.sizeColor.product.discount;

            int quantity = cart.quantity;

            if (isCheck) totalPrice += finalPrice * quantity;
        }

        listener.onTotalPriceChange(totalPrice);
        System.out.println("Total price calculate: " + totalPrice);
    }

    public void updateCartItem(String cartId, int quantity) {
        for (MyPair c : carts) {
            Cart cart = c.first;
            if (Utils.compare(cart._id, cartId)) {
                if (quantity == 0) {
                    carts.remove(c);
                    notifyDataSetChanged();
                } else {
                    int index = carts.indexOf(c);
                    //TODO update quantity
                    notifyItemChanged(index);
                }
                break;
            }
        }
        calculateTotalPrice();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding binding = ItemCartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ItemCartBinding binding = holder.binding;
            Cart cartProduct = carts.get(position).first;
            MyPair pair = carts.get(position);

            int originPrice = cartProduct.sizeColor.product.price;

            double discount = 0;
            if (cartProduct.sizeColor.product.discount != null) {
                discount = (double) cartProduct.sizeColor.product.discount;
            }

            int quantity = cartProduct.quantity;
            String colorName = cartProduct.sizeColor.color.colorName;
            String sizeName = cartProduct.sizeColor.size.sizeName;

            binding.textName.setText(cartProduct.sizeColor.product.name);
            binding.textQuantity.setText(Integer.toString(quantity));

            if (discount != 0) {
                binding.textOriginPrice.setVisibility(View.VISIBLE);
                binding.textPrice.setText(Double.toString(discount));
                binding.textOriginPrice.setText(Integer.toString(originPrice));
                binding.textOriginPrice.setPaintFlags(binding.textOriginPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.textOriginPrice.setVisibility(View.INVISIBLE);
                binding.textPrice.setText(Integer.toString(originPrice));
            }

            binding.textSizeColor.setText(sizeName + ", " + colorName);

            Glide.with(holder.itemView.getContext()).load(cartProduct.sizeColor.product.image.get(0)).error(R.drawable.ic_image_error).centerCrop().into(binding.imageProduct);

            binding.buttonMinus.setOnClickListener(v -> {
                cartProduct.quantity--;
                listener.onQuantityMinus(cartProduct._id, cartProduct.quantity);
            });

            binding.buttonPlus.setOnClickListener(v -> {
                cartProduct.quantity++;
                listener.onQuantityPlus(cartProduct._id, cartProduct.quantity);
            });

            holder.itemView.setOnClickListener(v -> listener.onItemClick(cartProduct.sizeColor.product._id));

            binding.checkboxChoose.setChecked(pair.second);
            binding.checkboxChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                pair.setSecond(isChecked);
                calculateTotalPrice();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (carts == null) return 0;
        return carts.size();
    }

    public String[] getCartIdArray() {
        List<String> list = new ArrayList<>();
        for (MyPair pair : carts) {
            Cart cart = pair.first;
            boolean isCheck = pair.second;

            if (isCheck) list.add(cart._id);
        }
        return list.toArray(new String[0]);
    }

    public interface OnItemInteractListener {
        void onQuantityMinus(String cartId, int afterQuantity);

        void onQuantityPlus(String cartId, int afterQuantity);

        void onItemClick(String productId);

        void onTotalPriceChange(int totalPrice);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public ViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class MyPair {
        private Cart first;
        private boolean second;

        public MyPair(Cart first, boolean second) {
            this.first = first;
            this.second = second;
        }

        public Cart getFirst() {
            return first;
        }

        public void setFirst(Cart newCart) {
            first = newCart;
        }

        public boolean getSecond() {
            return second;
        }

        public void setSecond(boolean n) {
            second = n;
        }
    }
}
