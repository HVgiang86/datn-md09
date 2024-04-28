package poly.manhnt.datn_md09.Presenters.HomePresenter.MenuPresenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import poly.manhnt.datn_md09.ConnectInternet.DownloadJson;
import poly.manhnt.datn_md09.DataManager;
import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.HomeModel.MenuModel.MenuModel;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;
import poly.manhnt.datn_md09.Views.HomeScreen.MenuView;
import poly.manhnt.datn_md09.api.ApiService;
import poly.manhnt.datn_md09.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuPresenter implements IPresenterMenu {
    MenuView menuView;

    public MenuPresenter(MenuView menuView) {
        this.menuView = menuView;
    }


    @Override
    public void LayDanhSachMenu() {
        List<LoaiSanPham> loaiSanPhamList;
        String dataJson = "";
        List<HashMap<String, String>> attrs = new ArrayList<>();
        //Call data = GET
        //String duongdan = "http://192.168.1.101/serverFmodel/loaisanpham.php?maloaicha=0";
//        DownloadJson downloadJson = new DownloadJson(duongdan);
        //End method GET

        //Call data = POST
        String duongdan = "http://192.168.1.105/serverFmodel/loaisanpham.php";
        HashMap<String, String> hsMaLoaiCha = new HashMap<>();
        hsMaLoaiCha.put("maloaicha", "0");
        attrs.add(hsMaLoaiCha);
        DownloadJson downloadJson = new DownloadJson(duongdan, attrs);
        //End POST

        downloadJson.execute();

        try {
            dataJson = downloadJson.get();
            Log.d("HEHE", dataJson);
            MenuModel menuModel = new MenuModel();
            loaiSanPhamList = menuModel.parsetJSONmenu(dataJson);
            menuView.HienThiDanhSachMenu(loaiSanPhamList);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getCategories() {
        try {
            Log.d("HEHE", "get categories");
            RetrofitClient.getInstance().create(ApiService.class).getCategories().enqueue(new Callback<CategoryIdResponse>() {
                @Override
                public void onResponse(Call<CategoryIdResponse> call, Response<CategoryIdResponse> response) {
                    if (response.isSuccessful()) {
                        DataManager.getInstance().categories = response.body().categories;
                        menuView.onGetCategoriesSuccess(response.body().categories);
                    }
                }

                @Override
                public void onFailure(Call<CategoryIdResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
