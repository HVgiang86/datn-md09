package poly.manhnt.datn_md09.Views.DeliveryMethodScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import poly.manhnt.datn_md09.Adapters.DeliveryMethodAdapter;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.databinding.ActivityDeliveryMethodBinding;

public class DeliveryMethodActivity extends AppCompatActivity implements DeliveryMethodAdapter.OnItemClickListener {
    public static final String KEY_DELIVERY_METHOD = "key_delivery_method";
    public static final int REQUEST_CODE = 112;

    private int methodId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poly.manhnt.datn_md09.databinding.ActivityDeliveryMethodBinding binding = ActivityDeliveryMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra(KEY_DELIVERY_METHOD))
            methodId = getIntent().getIntExtra(KEY_DELIVERY_METHOD, 0);


        DeliveryMethodAdapter adapter = new DeliveryMethodAdapter(DataManager.getInstance().deliveryMethods, this, methodId);
        adapter.setOnItemClickListener(this);
        binding.recycler.setAdapter(adapter);

        binding.toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void onClick(int methodId) {
        this.methodId = methodId;
        Intent intent = new Intent();
        intent.putExtra(KEY_DELIVERY_METHOD, methodId);
        setResult(RESULT_OK, intent);
        finish();
    }
}