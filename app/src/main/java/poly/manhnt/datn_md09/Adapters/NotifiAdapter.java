package poly.manhnt.datn_md09.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.Models.UserNotification.UserNotification;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.databinding.CustomCardviewNotifiBinding;
import poly.manhnt.datn_md09.utils.Utils;

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.ViewHolder> {
    Context context;
    List<UserNotification> list;

    public NotifiAdapter(Context context, List<UserNotification> list) {
        this.context = context;
        this.list = list;
    }

    public void updateData(List<UserNotification> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void updateNotification(String notificationId) {
        for (UserNotification n : list) {
            if (Utils.compare(n.id, notificationId)) {
                int index = list.indexOf(n);
                n.status = false;
                notifyItemChanged(index);
            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomCardviewNotifiBinding binding = CustomCardviewNotifiBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserNotification notification = list.get(position);
        holder.binding.title.setText(notification.title);
        holder.binding.textContent.setText(notification.content);

        Glide.with(holder.itemView.getContext()).load(notification.image).placeholder(R.drawable.ic_notifications).centerCrop().into(holder.binding.imageNotice);

        if (notification.status)
            holder.binding.container.setBackgroundColor(holder.itemView.getContext().getColor(R.color.gray));
        else
            holder.binding.container.setBackgroundColor(holder.itemView.getContext().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CustomCardviewNotifiBinding binding;
        TextView textView;
        CardView cardView;

        public ViewHolder(@NonNull CustomCardviewNotifiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

