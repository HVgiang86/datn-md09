package poly.manhnt.datn_md09;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Models.DeliveryMethod.DeliveryMethod;
import poly.manhnt.datn_md09.Models.ProductCategory;
import poly.manhnt.datn_md09.Models.ProductQuantity.ProductQuantity;
import poly.manhnt.datn_md09.Models.model_login.LoginResponse;

public class DataManager {
    private static DataManager instance;
    public LoginResponse getUserLogin;
    public List<ProductCategory> categories;
    public List<ProductQuantity> productQuantityList = new ArrayList<>();

    public List<DeliveryMethod> deliveryMethods = new ArrayList<>();

    private DataManager() {
        deliveryMethods.add(new DeliveryMethod(0, "Tiêu chuẩn", "Nhận hàng sau 3-5 ngày", 12000));
        deliveryMethods.add(new DeliveryMethod(1, "Nhanh", "Nhận hàng vào ngày mai", 24000));
        deliveryMethods.add(new DeliveryMethod(2, "Hoả tốc", "Nhận hàng trong ngày", 50000));
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
}
