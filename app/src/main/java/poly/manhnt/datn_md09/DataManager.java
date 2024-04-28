package poly.manhnt.datn_md09;

import java.util.List;

import poly.manhnt.datn_md09.Models.CategoryIdResponse;
import poly.manhnt.datn_md09.Models.ProductCategory;
import poly.manhnt.datn_md09.Models.model_login.LoginResponse;

public class DataManager {
    private static DataManager instance;

    private DataManager(){}

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public LoginResponse getUserLogin;
    public List<ProductCategory> categories;
}
