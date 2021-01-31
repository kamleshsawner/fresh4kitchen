package singa.tech.fresh4kitchen.model;

public class MyOrder {
    private String id;
    private String order_id;
    private String name;
    private String price;
    private String qty;
    private String varient;
    private String image;
    private String date;
    private String time;
    private String status;

    public MyOrder(String id, String order_id, String name, String price, String qty, String varient, String image, String date, String time, String status) {
        this.id = id;
        this.order_id = order_id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.varient = varient;
        this.image = image;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getVarient() {
        return varient;
    }

    public void setVarient(String varient) {
        this.varient = varient;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
