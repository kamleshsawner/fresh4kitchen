package singa.tech.fresh4kitchen.model;

public class Cart {

    private String id;
    private String user_id;
    private String product_id;
    private String verient_id;
    private String unit;
    private String qty;
    private String price;
    private String name;
    private String cover_img;

    public Cart(String id, String user_id, String product_id, String verient_id, String unit, String qty, String price, String name, String cover_img) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.verient_id = verient_id;
        this.unit = unit;
        this.qty = qty;
        this.price = price;
        this.name = name;
        this.cover_img = cover_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVerient_id() {
        return verient_id;
    }

    public void setVerient_id(String verient_id) {
        this.verient_id = verient_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }
}
