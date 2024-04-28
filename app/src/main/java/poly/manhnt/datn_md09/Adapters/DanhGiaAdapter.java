package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.Models.ProductComment.ProductComment;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.Views.DetailScreen.ImageViewPagerAdapter;
import poly.manhnt.datn_md09.databinding.CustomCardviewDanhgiaBinding;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {
    Context context;
    List<ProductComment> productComments;

    public DanhGiaAdapter(Context context, List<ProductComment> productComments) {
        this.context = context;
        this.productComments = productComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_cardview_danhgia, parent, false);

        CustomCardviewDanhgiaBinding binding = CustomCardviewDanhgiaBinding.inflate(layoutInflater);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductComment pc = productComments.get(position);

        if (pc.user_id != null) {
            holder.binding.textUname.setText(pc.user_id.full_name);
            Glide.with(context).load(pc.user_id.avata).placeholder(R.drawable.ic_user).centerCrop().into(holder.binding.imageAvatar);

        } else {
            holder.binding.textUname.setText("Người dùng ẩn danh");
        }
        holder.binding.textRating.setText(pc.rating + " sao");

        if (pc.images.isEmpty()) {
            holder.binding.imageProduct.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(pc.images.get(0)).error(R.drawable.ic_image_error).centerCrop().into(holder.binding.imageProduct);
        }

        holder.binding.textType.setText("Phân loại: " + pc.product_detail_id.color_id.name + " - " + pc.product_detail_id.size_id.name);

        if (pc.comment.isEmpty()) {
            holder.binding.textComment.setVisibility(View.GONE);
        } else {
            holder.binding.textComment.setText(pc.comment);
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

