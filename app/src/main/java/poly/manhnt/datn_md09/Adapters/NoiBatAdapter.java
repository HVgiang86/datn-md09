package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
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

import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.CustomRecyclerNoiBatBinding;


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
        Glide.with(context).load(productResponse.image.get(0)).placeholder(R.drawable.backgroundplashscreen).error(R.drawable.backgroundplashscreen).into(holder.imvBackground);
        holder.tvPrice.setText(productResponse.price + "$");

        holder.itemView.setOnClickListener(v -> {
            String id = list.get(position)._id;
            onItemClickListener.onLick(id);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
