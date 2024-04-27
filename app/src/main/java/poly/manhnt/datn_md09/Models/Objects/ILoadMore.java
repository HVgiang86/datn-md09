package poly.manhnt.datn_md09.Models.Objects;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ILoadMore extends RecyclerView.OnScrollListener {
    int itemAnDauTien = 0;
    int tongItem = 0;
    int itemLoadTruoc = 10;
    RecyclerView.LayoutManager layoutManager;
    public ILoadMore(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        tongItem = layoutManager.getItemCount();
        itemAnDauTien = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        if(tongItem <= (itemAnDauTien + itemLoadTruoc)){
            Log.d("zzzz", tongItem +" "+ itemAnDauTien);
        }

    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}
