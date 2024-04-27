package poly.manhnt.datn_md09.Views.PayScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.NotifiAdapter;
import poly.manhnt.datn_md09.R;


public class PayActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotifiAdapter notifiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        recyclerView = findViewById(R.id.recyclerPay);

        List<String> list = new ArrayList<>();
        for(int i=0; i<20; i++){
            String ten = "Sản phẩm "+ i;
            list.add(ten);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        notifiAdapter = new NotifiAdapter(this, list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notifiAdapter);
        notifiAdapter.notifyDataSetChanged();
    }
}