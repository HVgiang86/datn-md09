package poly.manhnt.datn_md09.Views.DetailScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.ItemImageViewpagerBinding;

public class ImageViewPagerAdapter extends RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder> {
    private final List<String> imageList;

    public ImageViewPagerAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemImageViewpagerBinding binding = ItemImageViewpagerBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(imageList.get(position)).error(R.drawable.ic_image_error).centerCrop().into(holder.binding.imageProduct);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemImageViewpagerBinding binding = null;

        public ViewHolder(@NonNull ItemImageViewpagerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
