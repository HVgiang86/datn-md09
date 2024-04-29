package poly.manhnt.datn_md09.Views.HomeScreen;

import android.app.Activity;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.Objects.ILoadMore;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;
import poly.manhnt.datn_md09.Models.ProductCategory;
import poly.manhnt.datn_md09.Models.ProductQuantity.ProductQuantity;
import poly.manhnt.datn_md09.Models.ProductResponse;
import poly.manhnt.datn_md09.Presenters.HomePresenter.MenuPresenter.MenuPresenter;
import poly.manhnt.datn_md09.Presenters.HomePresenter.ProductPresent.ProductContract;
import poly.manhnt.datn_md09.Presenters.HomePresenter.ProductPresent.ProductPresenter;
import poly.manhnt.datn_md09.R;
import poly.manhnt.datn_md09.Views.AcountScreen.AcountActivity;
import poly.manhnt.datn_md09.Views.CartScreen.CartActivity;
import poly.manhnt.datn_md09.Views.DetailScreen.DetailActivity;
import poly.manhnt.datn_md09.Views.NotifiScreen.NotifiActivity;
import poly.manhnt.datn_md09.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements NoiBatAdapter.OnProductClickListener, MenuView, ProductContract.View {
    public static String EXTRA_PRODUCT_ID = "productId";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final List<ProductResponse> displayList = new ArrayList();
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

    private PriceSortMode priceSortMode = PriceSortMode.UNSORTED;
    private List<ProductResponse> productResponseList;
    private MenuPresenter menuPresenter;
    private ProductPresenter productPresenter;
    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(mBinding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawerLayout);
        expandableListView = findViewById(R.id.epMenu);
        scrollView = findViewById(R.id.nestedScrollHome);
        recyclerView = findViewById(R.id.recyclerNoiBat);
        productResponseList = new ArrayList<>();

        mBinding.btnHomeNotifi.setOnClickListener(v -> {
            switchScreen(NotifiActivity.class);
        });
        mBinding.btnHomeAcount.setOnClickListener(v -> {
            switchScreen(AcountActivity.class);
        });

        //TODO: MinhNTn fake data
        fakeDataProduct();

        menuPresenter = new MenuPresenter(this);
        productPresenter = new ProductPresenter(this);
//        menuPresenter.LayDanhSachMenu();


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

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
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

            if (scrollY > 270) {
                showStickyFilterBar();
            } else {
                hideStickyFilterBar();
            }
        });

        mBinding.textPriceSort.setOnClickListener(v -> {
            if (priceSortMode == PriceSortMode.UNSORTED) priceSortMode = PriceSortMode.ASC;
            else if (priceSortMode == PriceSortMode.ASC) priceSortMode = PriceSortMode.DESC;
            else if (priceSortMode == PriceSortMode.DESC) priceSortMode = PriceSortMode.ASC;

            if (priceSortMode == PriceSortMode.ASC) {
                mBinding.textPriceSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_asc, 0);
                mBinding.textPriceSortFixed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_asc, 0);
                sortPriceAsc();
            }
            if (priceSortMode == PriceSortMode.DESC) {
                mBinding.textPriceSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_desc, 0);
                mBinding.textPriceSortFixed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_desc, 0);
                sortPriceDesc();
            }
        });

        mBinding.textPriceSortFixed.setOnClickListener(v -> {
            if (priceSortMode == PriceSortMode.UNSORTED) priceSortMode = PriceSortMode.ASC;
            else if (priceSortMode == PriceSortMode.ASC) priceSortMode = PriceSortMode.DESC;
            else if (priceSortMode == PriceSortMode.DESC) priceSortMode = PriceSortMode.ASC;

            if (priceSortMode == PriceSortMode.ASC) {
                mBinding.textPriceSortFixed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_asc, 0);
                mBinding.textPriceSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_asc, 0);
                sortPriceAsc();
            }
            if (priceSortMode == PriceSortMode.DESC) {
                mBinding.textPriceSortFixed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_desc, 0);
                mBinding.textPriceSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_price_desc, 0);
                sortPriceDesc();
            }
        });

        mBinding.searchEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchByName(mBinding.searchEdt.getText().toString().trim());
                hideKeyboard();
                scrollView.scrollTo(0, 0);

                return true;
            }
            return false;
        });

    }

    private <T> void switchScreen(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void sortPriceAsc() {
        scrollView.scrollTo(0, 0);
        noiBatAdapter.sortPriceAsc();
    }

    private void sortPriceDesc() {
        scrollView.scrollTo(0, 0);
        noiBatAdapter.sortPriceDesc();
    }

    private void hideStickyFilterBar() {
        mBinding.filterContainer.setVisibility(View.VISIBLE);
        mBinding.filterContainerFixed.setVisibility(View.INVISIBLE);
    }

    private void showStickyFilterBar() {
        mBinding.filterContainer.setVisibility(View.INVISIBLE);
        mBinding.filterContainerFixed.setVisibility(View.VISIBLE);
    }

    private void initData() {
        menuPresenter.getCategories();
        productPresenter.getProductPage(3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("Click menu");
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        if (item.getItemId() == R.id.itCart) {
            System.out.println("Click cart");
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAutoScroll() {
        handler.postDelayed(() -> {
            currentPosition++;
            if (currentPosition >= viewPagerAdapter.getCount()) {
                currentPosition = 0;
            }
            viewPager.setCurrentItem(currentPosition);
            startAutoScroll();
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

    @Override
    public void HienThiDanhSachMenu(List<LoaiSanPham> loaiSanPhamList) {
        //TODO Implement
    }

    @Override
    public void onGetCategoriesSuccess(List<ProductCategory> categories) {
        initFilterBar(categories);
        System.out.println("Init filterbar");
        scrollView.scrollTo(0, 0);
    }

    public void initFilterBar(List<ProductCategory> categories) {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả");
        for (ProductCategory c : categories) {
            categoryNames.add(c.name);
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        mBinding.spinnerCategory.setAdapter(categoryAdapter);
        mBinding.spinnerCategoryFixed.setAdapter(categoryAdapter);

        mBinding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.spinnerCategoryFixed.setSelection(position);
                filterByCategory(categoryNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.spinnerCategoryFixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.spinnerCategory.setSelection(position);
                filterByCategory(categoryNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onGetProductPageSuccess(int page, List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;

        noiBatAdapter.updateData(productResponseList);

        for (ProductResponse pr : productResponseList) {
            productPresenter.getProductQuantity(pr._id);
        }
    }

    @Override
    public void onSearchProductSuccess(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
        noiBatAdapter.updateData(productResponseList);

        for (ProductResponse pr : productResponseList) {
            productPresenter.getProductQuantity(pr._id);
        }
    }

    @Override
    public void onGetProductQuantitySuccess(String productId, int quantity) {
        ProductQuantity pq = new ProductQuantity();
        pq.productId = productId;
        pq.quantity = quantity;

        DataManager.getInstance().productQuantityList.add(pq);
        int index = -1;
        for (ProductResponse pr : productResponseList) {
            if (pr._id.equals(productId)) index = productResponseList.indexOf(pr);
        }
        if (index != -1) {
            noiBatAdapter.notifyItemChanged(index);
        }
    }

    private void filterByCategory(String categoryName) {
        String categoryId = "";
        for (ProductCategory pc : DataManager.getInstance().categories) {
            if (pc.name.equals(categoryName)) {
                categoryId = pc._id;
                break;
            }
        }
        if (categoryName.equals("Tất cả")) {
            noiBatAdapter.updateData(productResponseList);
        } else {
            displayList.clear();
            for (ProductResponse pr : productResponseList) {
                if (pr.category_id._id.equals(categoryId)) {
                    displayList.add(pr);
                }
            }

            noiBatAdapter.updateData(displayList);
        }
    }

    private void searchByName(String s) {
        if (s.isEmpty()) productPresenter.getProductPage(2);
        else productPresenter.searchProductByName(s);
    }
}