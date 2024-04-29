package poly.manhnt.datn_md09.Models.discount;

import com.google.gson.annotations.SerializedName;

public class UserDiscount {
    @SerializedName("_id")
    public String id;
    @SerializedName("price")
    public int discount;
    @SerializedName("start_day")
    public String startDateString;
    @SerializedName("end_day")
    public String endDateString;
    @SerializedName("description")
    public String description;
    @SerializedName("usageCount")
    public int usageCount;

}
