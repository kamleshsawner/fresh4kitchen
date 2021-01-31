package singa.tech.fresh4kitchen.model;

public class Products {

    private String id;
    private String category_id;
    private String name;
    private String cover_img;
    private String product_img1;
    private String product_img2;
    private String product_img3;
    private String product_img4;
    private String about;
    private String benifit;
    private String featured;
    private String category_name;
    private String unit;
    private String price;
    private String offer_price;
    private String percentage;
    private String verient_id;

    public Products(String id, String category_id, String name, String cover_img, String product_img1, String product_img2, String product_img3, String product_img4, String about, String benifit, String featured, String category_name, String unit, String price, String offer_price, String percentage, String verient_id) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.cover_img = cover_img;
        this.product_img1 = product_img1;
        this.product_img2 = product_img2;
        this.product_img3 = product_img3;
        this.product_img4 = product_img4;
        this.about = about;
        this.benifit = benifit;
        this.featured = featured;
        this.category_name = category_name;
        this.unit = unit;
        this.price = price;
        this.offer_price = offer_price;
        this.percentage = percentage;
        this.verient_id = verient_id;
    }

    public String getVerient_id() {
        return verient_id;
    }

    public void setVerient_id(String verient_id) {
        this.verient_id = verient_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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

    public String getProduct_img1() {
        return product_img1;
    }

    public void setProduct_img1(String product_img1) {
        this.product_img1 = product_img1;
    }

    public String getProduct_img2() {
        return product_img2;
    }

    public void setProduct_img2(String product_img2) {
        this.product_img2 = product_img2;
    }

    public String getProduct_img3() {
        return product_img3;
    }

    public void setProduct_img3(String product_img3) {
        this.product_img3 = product_img3;
    }

    public String getProduct_img4() {
        return product_img4;
    }

    public void setProduct_img4(String product_img4) {
        this.product_img4 = product_img4;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBenifit() {
        return benifit;
    }

    public void setBenifit(String benifit) {
        this.benifit = benifit;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
