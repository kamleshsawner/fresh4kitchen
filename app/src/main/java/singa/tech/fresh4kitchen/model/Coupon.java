package singa.tech.fresh4kitchen.model;

public class Coupon {
    private String id;
    private String name;
    private String code;
    private String discount;
    private String date;

    public Coupon(String id, String name, String code, String discount, String date) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.discount = discount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
