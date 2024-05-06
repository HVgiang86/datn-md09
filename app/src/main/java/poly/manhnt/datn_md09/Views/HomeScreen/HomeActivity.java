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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import poly.manhnt.datn_md09.utils.Utils;

public class HomeActivity extends AppCompatActivity implements NoiBatAdapter.OnProductClickListener, MenuView, ProductContract.View {
    public static String EXTRA_PRODUCT_ID = "productId";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final List<ProductResponse> displayList = new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;
    ActionBarDrawerToggle drawerToggle;
    ExpandableListView expandableListView;
    NoiBatAdapter noiBatAdapter;
    int pageLimit = 4;
    List<String> categoryNames = new ArrayList<>();
    private int filterCategoryPosition = 0;
    private int pageNumber = 1;
    private int currentPosition = 0;
    private PriceSortMode priceSortMode = PriceSortMode.UNSORTED;
    private List<ProductResponse> productResponseList = new ArrayList<>();
    private MenuPresenter menuPresenter;
    private ProductPresenter productPresenter;
    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mBinding.getRoot());

        categoryNames.add("Tất cả");

        mBinding.btnHomeNotifi.setOnClickListener(v -> {
            switchScreen(NotifiActivity.class);
        });
        mBinding.btnHomeAcount.setOnClickListener(v -> {
            switchScreen(AcountActivity.class);
        });

        menuPresenter = new MenuPresenter(this);
        productPresenter = new ProductPresenter(this);
//        menuPresenter.LayDanhSachMenu();


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        noiBatAdapter = new NoiBatAdapter(this, productResponseList);
        noiBatAdapter.setOnItemClickListener(this);
        mBinding.recyclerNoiBat.setLayoutManager(layoutManager);
        mBinding.recyclerNoiBat.setAdapter(noiBatAdapter);
        mBinding.recyclerNoiBat.addOnScrollListener(new ILoadMore(layoutManager));

        initData();

        drawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, R.string.open, R.string.close);
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue1));
        mBinding.drawerLayout.addDrawerListener(drawerToggle);
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(viewPagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        startAutoScroll();

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        int scrollLimit = 256;

        mBinding.nestedScrollHome.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            float alpha = Math.min(1, (float) scrollY / scrollLimit);
            int alphaInt = (int) (alpha * 255);
            if (scrollY > oldScrollY) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.argb(alphaInt, 0, 169, 255));
                }
                mBinding.toolbar.setBackgroundColor(Color.argb(alphaInt, 0, 169, 255));
                drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            } else if (scrollY == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
                mBinding.toolbar.setBackgroundColor(Color.TRANSPARENT);
                drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue1));
            }

            if (scrollY > 380) {
                showStickyFilterBar();
            } else {
                hideStickyFilterBar();
            }

            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                pageNumber++;
                loadProductData(pageNumber);
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
                mBinding.nestedScrollHome.scrollTo(0, 0);
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
        mBinding.nestedScrollHome.scrollTo(0, 0);
        noiBatAdapter.sortPriceAsc();
    }

    private void sortPriceDesc() {
        mBinding.nestedScrollHome.scrollTo(0, 0);
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
        loadProductData(1);
    }


    private void loadProductData(int pageNumber) {
        if (pageNumber > pageLimit) return;
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        productPresenter.getProductPage(pageNumber);

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
            mBinding.viewPager.setCurrentItem(currentPosition);
            startAutoScroll();
        }, 3000);
    }

    @Override
    public void onLick(String productId) {
        startDetailActivity(productId);
    }

    private void startDetailActivity(String productId) {
        System.out.println("Open detail, id: " + productId);
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
        mBinding.nestedScrollHome.scrollTo(0, 0);
    }

    @Override
    public void onGetProductPageFail(int page) {
        mBinding.progressCircular.setVisibility(View.GONE);
        pageLimit = page - 1;
    }

    public void initFilterBar(List<ProductCategory> categories) {
        for (ProductCategory c : categories) {
            categoryNames.add(c.name);
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        mBinding.spinnerCategory.setAdapter(categoryAdapter);
        mBinding.spinnerCategoryFixed.setAdapter(categoryAdapter);

        mBinding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCategoryPosition = position;
                mBinding.spinnerCategoryFixed.setSelection(position);
                filterByCategory(categoryNames.get(filterCategoryPosition));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.spinnerCategoryFixed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCategoryPosition = position;
                mBinding.spinnerCategory.setSelection(position);
                filterByCategory(categoryNames.get(filterCategoryPosition));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onGetProductPageSuccess(int page, List<ProductResponse> productResponseList) {
        mBinding.progressCircular.setVisibility(View.GONE);
        System.out.println("Before size: " + this.productResponseList.size());
        this.productResponseList.addAll(productResponseList);
        System.out.println("After size: " + this.productResponseList.size());

        filterByCategory(categoryNames.get(filterCategoryPosition));
        if (priceSortMode == PriceSortMode.ASC) sortPriceAsc();
        else if (priceSortMode == PriceSortMode.DESC) sortPriceDesc();

        for (ProductResponse pr : productResponseList) {
            productPresenter.getProductQuantity(pr._id);
        }
    }

    @Override
    public void onSearchProductSuccess(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
        for (ProductResponse pr : productResponseList) {
            System.out.println("Search success: " + pr.toString() + "\t" + pr._id);
        }

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
            if (Utils.compare(pr._id, productId)) index = productResponseList.indexOf(pr);
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