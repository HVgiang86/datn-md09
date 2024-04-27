package poly.manhnt.datn_md09.Models.HomeModel.MenuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import poly.manhnt.datn_md09.ConnectInternet.DownloadJson;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;


public class MenuModel {
    public List<LoaiSanPham> parsetJSONmenu(String dulieujson){
        List<LoaiSanPham> loaiSanPhamList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(dulieujson);
            JSONArray loaisanpham = jsonObject.getJSONArray("LOAISANPHAM");
            int count = loaisanpham.length();
            for(int i=0; i<count; i++){
                JSONObject value = loaisanpham.getJSONObject(i);

                LoaiSanPham dataLoaiSanPham = new LoaiSanPham();
                dataLoaiSanPham.setMALOAISP(Integer.parseInt(value.getString("MALOAISP")));
                dataLoaiSanPham.setMALOAICHA(Integer.parseInt(value.getString("MALOAICHA")));
                dataLoaiSanPham.setTENLOAISP(value.getString("TENLOAISP"));

                loaiSanPhamList.add(dataLoaiSanPham);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return loaiSanPhamList;
    }
    public List<LoaiSanPham> layLoaiSPtheoMaLoai (int maloaisp){
        List<LoaiSanPham> loaiSanPhamList;
        List<HashMap<String, String>> attrs = new ArrayList<>();
        String dataJson = "";
        String duongdan = "http://192.168.25.131/serverFmodel/loaisanpham.php";
        HashMap<String, String> hsMaLoaiCha = new HashMap<>();
        hsMaLoaiCha.put("maloaicha", String.valueOf(maloaisp));
        attrs.add(hsMaLoaiCha);
        DownloadJson downloadJson = new DownloadJson(duongdan, attrs);

        downloadJson.execute();
        try {
            dataJson = downloadJson.get();
            MenuModel menuModel = new MenuModel();
            loaiSanPhamList = menuModel.parsetJSONmenu(dataJson);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return loaiSanPhamList;
    }
}
