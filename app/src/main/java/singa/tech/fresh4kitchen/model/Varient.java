package singa.tech.fresh4kitchen.model;

public class Varient {
    private String id;
    private String product_id;
    private String varient_id;
    private String offer_price;
    private String actual_price;
    private String weight;
    private String percentage;

    public Varient(String id, String product_id, String varient_id, String offer_price, String actual_price, String weight, String percentage) {
        this.id = id;
        this.product_id = product_id;
        this.varient_id = varient_id;
        this.offer_price = offer_price;
        this.actual_price = actual_price;
        this.weight = weight;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
