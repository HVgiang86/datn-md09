package poly.manhnt.datn_md09.Presenters.HomePresenter.MenuPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import poly.manhnt.datn_md09.ConnectInternet.DownloadJson;
import poly.manhnt.datn_md09.Models.HomeModel.MenuModel.MenuModel;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;
import poly.manhnt.datn_md09.Views.HomeScreen.MenuView;

public class MenuPresenter implements IPresenterMenu{
    MenuView menuView;
    public MenuPresenter(MenuView menuView){
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
        hsMaLoaiCha.put("maloaicha","0");
        attrs.add(hsMaLoaiCha);
        DownloadJson downloadJson = new DownloadJson(duongdan, attrs);
        //End POST

        downloadJson.execute();

        try {
            dataJson = downloadJson.get();
            MenuModel menuModel = new MenuModel();
            loaiSanPhamList = menuModel.parsetJSONmenu(dataJson);
            menuView.HienThiDanhSachMenu(loaiSanPhamList);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
