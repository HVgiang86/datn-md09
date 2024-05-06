package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Const.AppConfig;
import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.Models.cart.ProductCart;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.CustomCardviewDanhgiaBinding;
import poly.manhnt.datn_md09.utils.Utils;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {
    private final List<ProductCart> productCarts = new ArrayList<>();
    Context context;
    List<ProductComment> productComments;

    public DanhGiaAdapter(Context context, List<ProductComment> productComments) {
        this.context = context;
        this.productComments = productComments;
    }

    public void addComment(ProductComment comment) {
        productComments.add(comment);
        notifyItemChanged(productComments.size() - 1);
    }

    public void addProductCart(ProductCart productCart) {
        productCarts.add(productCart);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CustomCardviewDanhgiaBinding binding = CustomCardviewDanhgiaBinding.inflate(layoutInflater, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductComment pc = productComments.get(position);

        if (pc.user_id != null) {
            holder.binding.textUname.setText(pc.user_id.full_name);
            String imageUrl = AppConfig.API_URL + pc.user_id.avata;
            Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_user).centerCrop().into(holder.binding.imageAvatar);

        } else {
            holder.binding.textUname.setText("Người dùng ẩn danh");
        }
        holder.binding.textRating.setText(pc.rating + " sao");

        if (pc.images.isEmpty()) {
            holder.binding.imageProduct.setVisibility(View.GONE);
        } else {
            String imageUrl = AppConfig.API_URL + pc.images.get(0);
            Glide.with(context).load(imageUrl).error(R.drawable.ic_image_error).centerCrop().into(holder.binding.imageProduct);
        }

        holder.binding.textType.setText("Phân loại: " + pc.product_detail_id.color_id.name + " - " + pc.product_detail_id.size_id.name);

        if (pc.comment.isEmpty()) {
            holder.binding.textComment.setVisibility(View.GONE);
        } else {
            holder.binding.textComment.setText(pc.comment);
        }

        if (!productCarts.isEmpty()) {
            holder.binding.containerProductPreview.setVisibility(View.VISIBLE);
            String productId = pc.product_detail_id.product_id;

            for (ProductCart productCart : productCarts) {
                if (Utils.compare(productCart._id, productId)) {
                    holder.binding.textProductName.setText(productCart.name);
                    Glide.with(context).load(productCart.image.get(0)).error(R.drawable.ic_image_error).centerCrop().into(holder.binding.imageProductPreview);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return productComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomCardviewDanhgiaBinding binding;


        public ViewHolder(@NonNull CustomCardviewDanhgiaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

