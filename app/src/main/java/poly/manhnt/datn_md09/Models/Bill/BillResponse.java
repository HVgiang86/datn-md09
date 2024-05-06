package poly.manhnt.datn_md09.Models.Bill;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillResponse {
    @SerializedName("data")
    public List<Bill> billList;
}
