package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.ProductQuantity.ProductQuantity;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.CustomRecyclerNoiBatBinding;
import poly.manhnt.datn_md09.utils.Utils;


public class NoiBatAdapter extends RecyclerView.Adapter<NoiBatAdapter.ViewHolder> {
    Context context;
    List<ProductResponse> list;
    OnProductClickListener onItemClickListener;

    public NoiBatAdapter(Context context, List<ProductResponse> list) {
        this.context = context;
        this.list = list;
    }

    public void updateData(List<ProductResponse> list) {
        this.list = list;
        calculateFinalPrice();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnProductClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomRecyclerNoiBatBinding binding = CustomRecyclerNoiBatBinding.inflate(layoutInflater, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductResponse productResponse = list.get(position);
        holder.textView.setText(productResponse.name);

        CustomRecyclerNoiBatBinding binding = holder.binding;

        //Price, discount and original price
        if (productResponse.discount == null) {
            //no discount

            binding.textOriginPrice.setVisibility(View.INVISIBLE);
            binding.tvPrice.setText("" + productResponse.price);
        } else {
            binding.textOriginPrice.setVisibility(View.VISIBLE);
            binding.tvPrice.setText("VNĐ " + productResponse.discount);
            binding.textOriginPrice.setText("VNĐ " + productResponse.price);
            binding.textOriginPrice.setPaintFlags(binding.textOriginPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        List<ProductQuantity> quantities = DataManager.getInstance().productQuantityList;

        boolean check = false;
        for (ProductQuantity pq : quantities) {
            if (Utils.compare(productResponse._id, pq.productId)) {
                if (pq.quantity == 0) {
                    holder.binding.imvBackground.setVisibility(View.GONE);
                    holder.binding.containerSoldOut.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext()).load(productResponse.image.get(0)).placeholder(R.drawable.backgroundplashscreen).error(R.drawable.backgroundplashscreen).into(holder.imvBackground);
                } else {
                    holder.binding.imvBackground.setVisibility(View.VISIBLE);
                    holder.binding.containerSoldOut.setVisibility(View.GONE);
                    Glide.with(context).load(productResponse.image.get(0)).placeholder(R.drawable.backgroundplashscreen).error(R.drawable.backgroundplashscreen).into(holder.imvBackground);
                }
                check = true;
            }
        }

        if (!check) {
            holder.binding.imvBackground.setVisibility(View.VISIBLE);
            holder.binding.containerSoldOut.setVisibility(View.GONE);
            Glide.with(context).load(productResponse.image.get(0)).placeholder(R.drawable.backgroundplashscreen).error(R.drawable.backgroundplashscreen).into(holder.imvBackground);
        }

        holder.itemView.setOnClickListener(v -> {
            String id = list.get(position)._id;
            onItemClickListener.onLick(id);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void calculateFinalPrice() {
        for (ProductResponse pr : list) {
            if (pr.discount == null) pr.finalPrice = pr.price;
            else pr.finalPrice = pr.discount;
        }
    }

    public void sortPriceAsc() {
        Collections.sort(list, Comparator.comparingInt(ProductResponse::getPrice));

        list.forEach(productResponse -> {
            System.out.println(productResponse.name + "\t" + productResponse.price);
        });

        notifyDataSetChanged();
    }

    public void sortPriceDesc() {
        Collections.sort(list, Collections.reverseOrder(Comparator.comparingInt(ProductResponse::getPrice)));
        list.forEach(productResponse -> {
            System.out.println(productResponse.name + "\t" + productResponse.price);
        });

        notifyDataSetChanged();
    }

    public interface OnProductClickListener {
        void onLick(String productId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        TextView tvPrice;
        ImageView imvBackground;
        CustomRecyclerNoiBatBinding binding;

        public ViewHolder(@NonNull CustomRecyclerNoiBatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            textView = binding.txtTieuDeNoiBat;
            tvPrice = binding.tvPrice;
            imvBackground = binding.imvBackground;
        }
    }
}
