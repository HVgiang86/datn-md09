package poly.manhnt.datn_md09.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.databinding.ItemBillBinding;
import poly.manhnt.datn_md09.utils.Utils;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.ViewHolder> {
    public static final int TYPE_BILL_PENDING = 0;
    public static final int TYPE_BILL_PAID = 1;
    public static final int TYPE_BILL_DELIVERY = 2;
    public static final int TYPE_BILL_DELIVERED = 3;
    public static final int TYPE_BILL_CANCELED = 4;
    private final List<Bill> bills;
    private int viewType = TYPE_BILL_PENDING;

    private OnBillItemClickListener listener = null;

    public BillListAdapter(int viewType, List<Bill> bills) {
        this.viewType = viewType;
        this.bills = bills;
    }

    public void setBillItemClickListener(OnBillItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemBillBinding binding = ItemBillBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("view type: " + viewType);
        Bill bill = bills.get(position);

        holder.binding.textProductAmount.setText(bill.carts.size() + " sản phẩm");
        holder.binding.textTotalAmount.setText(bill.totalAmount + "đ");
        holder.binding.textProductSizeColor.setText(bill.carts.get(0).sizeColor.size.sizeName + ", " + bill.carts.get(0).sizeColor.color.colorName);
        holder.binding.textProductName.setText(bill.carts.get(0).sizeColor.product.name);
        holder.binding.textProductPrice.setText(bill.carts.get(0).sizeColor.product.getFinalPrice() + "đ");
        holder.binding.textProductQuantity.setText("x" + bill.carts.get(0).quantity);

        String createdAt = Utils.dateFormatter(bill.createdAt);

        if (createdAt != null) holder.binding.textCreateDate.setText(createdAt);
        else holder.binding.textCreateDate.setVisibility(View.GONE);

        Glide.with(holder.itemView.getContext()).load(bill.carts.get(0).sizeColor.product.image.get(0)).into(holder.binding.imagePreview);

        if (viewType == TYPE_BILL_PENDING) {
            holder.binding.buttonAction.setText("Huỷ");
            holder.binding.buttonAction.setOnClickListener(v -> {
                if (listener != null) listener.onCancelClick(bill);
            });

            if (bill.statusCode == 1) holder.binding.textBillStatus.setText("Chờ xác nhận");
            else if (bill.statusCode == 4) holder.binding.textBillStatus.setText("Chưa thanh toán");
        } else if (viewType == TYPE_BILL_PAID) {
            holder.binding.buttonAction.setText("Liên hệ shop");
            holder.binding.buttonAction.setOnClickListener(v -> {
                if (listener != null) listener.onContactShopClick(bill);
            });

            holder.binding.textBillStatus.setText("Chờ lấy hàng");
        } else if (viewType == TYPE_BILL_DELIVERY) {
            holder.binding.buttonAction.setText("Liên hệ shop");
            holder.binding.buttonAction.setOnClickListener(v -> {
                if (listener != null) listener.onContactShopClick(bill);
            });

            holder.binding.textBillStatus.setText("Đang vận chuyển");
        } else if (viewType == TYPE_BILL_DELIVERED) {
            holder.binding.buttonAction.setText("Đánh giá");
            holder.binding.buttonAction.setOnClickListener(v -> {
                if (listener != null) listener.onRateClick(bill);
            });


            holder.binding.textBillStatus.setText("Đã giao");
        } else {
            holder.binding.buttonAction.setText("Đặt lại");
            holder.binding.buttonAction.setOnClickListener(v -> {
                if (listener != null) listener.onReorder(bill);
            });


            if (bill.statusCode == 2) holder.binding.textBillStatus.setText("Đặt hàng thất bại");
            else if (bill.statusCode == 6)
                holder.binding.textBillStatus.setText("Vận chuyển thất bại");
            else if (bill.statusCode == 8)
                holder.binding.textBillStatus.setText("Người mua không nhận hàng");
            else holder.binding.textBillStatus.setText("Đã huỷ");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewBillClick(bill);
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public interface OnBillItemClickListener {
        void onCancelClick(Bill bill);

        void onContactShopClick(Bill bill);

        void onRateClick(Bill bill);

        void onReorder(Bill bill);

        void onViewBillClick(Bill bill);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemBillBinding binding;

        public ViewHolder(@NonNull ItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
