package poly.manhnt.datn_md09.Models.DeliveryMethod;

public class DeliveryMethod {
    public int id;
    public String name;
    public String timeTakenString;
    public int fee;

    public DeliveryMethod(int id, String name, String timeTakenString, int fee) {
        this.id = id;
        this.name = name;
        this.timeTakenString = timeTakenString;
        this.fee = fee;
    }
}
