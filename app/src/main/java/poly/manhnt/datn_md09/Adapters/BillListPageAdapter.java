package poly.manhnt.datn_md09.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import poly.manhnt.datn_md09.Models.Bill.Bill;
import poly.manhnt.datn_md09.databinding.LayoutPageBillStatusBinding;

public class BillListPageAdapter extends RecyclerView.Adapter<BillListPageAdapter.ViewHolder> {
    private List<Bill> bills = new ArrayList<>();

    private BillListAdapter.OnBillItemClickListener listener;

    public void setListener(BillListAdapter.OnBillItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Bill> bills) {
        this.bills = bills;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutPageBillStatusBinding binding = LayoutPageBillStatusBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            System.out.println("page 0");
            List<Bill> billList = bills.stream().filter(bill -> bill.statusCode == 1 || bill.statusCode == 4).collect(Collectors.toList());
            BillListAdapter adapter = new BillListAdapter(BillListAdapter.TYPE_BILL_PENDING, billList);
            adapter.setBillItemClickListener(listener);
            holder.binding.recyclerBill.setAdapter(adapter);
        } else if (position == 1) {
            System.out.println("page 1");
            List<Bill> billList = bills.stream().filter(bill -> bill.statusCode == 3).collect(Collectors.toList());
            BillListAdapter adapter = new BillListAdapter(BillListAdapter.TYPE_BILL_PAID, billList);
            adapter.setBillItemClickListener(listener);
            holder.binding.recyclerBill.setAdapter(adapter);
        } else if (position == 2) {
            System.out.println("page 2");
            List<Bill> billList = bills.stream().filter(bill -> bill.statusCode == 5).collect(Collectors.toList());
            BillListAdapter adapter = new BillListAdapter(BillListAdapter.TYPE_BILL_DELIVERY, billList);
            adapter.setBillItemClickListener(listener);
            holder.binding.recyclerBill.setAdapter(adapter);
        } else if (position == 3) {
            System.out.println("page 3");
            List<Bill> billList = bills.stream().filter(bill -> bill.statusCode == 7).collect(Collectors.toList());
            BillListAdapter adapter = new BillListAdapter(BillListAdapter.TYPE_BILL_DELIVERED, billList);
            adapter.setBillItemClickListener(listener);
            holder.binding.recyclerBill.setAdapter(adapter);
        } else {
            System.out.println("page 4");
            List<Bill> billList = bills.stream().filter(bill -> bill.statusCode == 2 || bill.statusCode == 6 || bill.statusCode == 8 || bill.statusCode == 9).collect(Collectors.toList());

            BillListAdapter adapter = new BillListAdapter(BillListAdapter.TYPE_BILL_CANCELED, billList);
            adapter.setBillItemClickListener(listener);
            holder.binding.recyclerBill.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPageBillStatusBinding binding;

        public ViewHolder(LayoutPageBillStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
