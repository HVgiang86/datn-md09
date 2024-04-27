package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.R;


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
        View view = layoutInflater.inflate(R.layout.custom_recycler_noi_bat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductResponse productResponse = list.get(position);
        holder.textView.setText(productResponse.description);
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

    public interface OnProductClickListener {
        void onLick(String productId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        TextView tvPrice;
        ImageView imvBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtTieuDeNoiBat);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imvBackground = itemView.findViewById(R.id.imvBackground);


        }
    }
}
