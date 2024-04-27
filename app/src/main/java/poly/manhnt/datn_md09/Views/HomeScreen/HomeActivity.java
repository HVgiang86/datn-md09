package poly.manhnt.datn_md09.Views.HomeScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.NoiBatAdapter;
import poly.manhnt.datn_md09.Adapters.ViewPagerAdapter;
import poly.manhnt.datn_md09.Models.Objects.ILoadMore;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.Views.CartScreen.CartActivity;
import poly.manhnt.datn_md09.Views.DetailScreen.DetailActivity;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NoiBatAdapter.OnProductClickListener {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ExpandableListView expandableListView;
    NestedScrollView scrollView;
    RecyclerView recyclerView;
    NoiBatAdapter noiBatAdapter;
    private int currentPosition = 0;
    private List<ProductResponse> productResponseList;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawerLayout);
        expandableListView = findViewById(R.id.epMenu);
        scrollView = findViewById(R.id.nestedScrollHome);
        recyclerView = findViewById(R.id.recyclerNoiBat);
        productResponseList = new ArrayList<>();

        //TODO: MinhNTn fake data
        fakeDataProduct();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        noiBatAdapter = new NoiBatAdapter(this, productResponseList);
        noiBatAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noiBatAdapter);
        recyclerView.addOnScrollListener(new ILoadMore(layoutManager));

        initData();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue1));
        drawerLayout.addDrawerListener(drawerToggle);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        startAutoScroll();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        int scrollLimit = 256;
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float alpha = Math.min(1, (float) scrollY / scrollLimit);
                int alphaInt = (int) (alpha * 255);
                if (scrollY > oldScrollY) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(Color.argb(alphaInt, 0, 169, 255));
                    }
                    toolbar.setBackgroundColor(Color.argb(alphaInt, 0, 169, 255));
                    drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
                } else if (scrollY == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue1));
                }
            }
        });
    }

    private void initData() {
        RetrofitClient.getInstance().create(ApiService.class).getListProduct().enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    noiBatAdapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.itCart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                if (currentPosition >= viewPagerAdapter.getCount()) {
                    currentPosition = 0;
                }
                viewPager.setCurrentItem(currentPosition);
                startAutoScroll();
            }
        }, 3000);
    }

    //TODO: MinhNTn fake data
    private void fakeDataProduct() {
        for (int i = 0; i < 10; i++) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.description = "Description " + i;
            productResponse.image = new ArrayList<>();
            productResponse.image.add("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg");
            productResponse.image.add("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg");
            productResponse.image.add("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg");
            productResponse.price = 1000;
            productResponseList.add(productResponse);
        }
    }

    @Override
    public void onLick(String productId) {
        startDetailActivity(productId);
    }

    private void startDetailActivity(String productId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        startActivity(intent);
    }

    public static String EXTRA_PRODUCT_ID = "productId";
}