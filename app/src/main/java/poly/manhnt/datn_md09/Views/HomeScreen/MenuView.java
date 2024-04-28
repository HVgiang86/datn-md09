package poly.manhnt.datn_md09.Views.HomeScreen;

import java.util.List;

import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;
import poly.manhnt.datn_md09.Models.ProductCategory;
import poly.manhnt.datn_md09.Models.ProductResponse;


public interface MenuView {
    void HienThiDanhSachMenu(List<LoaiSanPham> loaiSanPhamList);

    void onGetCategoriesSuccess(List<ProductCategory> categories);

}
