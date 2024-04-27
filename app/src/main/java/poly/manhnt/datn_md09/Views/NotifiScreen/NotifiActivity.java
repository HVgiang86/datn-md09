package poly.manhnt.datn_md09.Views.NotifiScreen;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.DanhGiaAdapter;
import poly.manhnt.datn_md09.Adapters.NotifiAdapter;
import poly.manhnt.datn_md09.R;


public class NotifiActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotifiAdapter notifiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);
        recyclerView = findViewById(R.id.recyclerNotifi);

        List<String> list = new ArrayList<>();
        for(int i=0; i<20; i++){
            String ten = "Thông báo "+ i;
            list.add(ten);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        notifiAdapter = new NotifiAdapter(this, list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notifiAdapter);
        notifiAdapter.notifyDataSetChanged();
    }
}