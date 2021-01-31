package singa.tech.fresh4kitchen.model;

public class Address {
    private String id;
    private String name;
    private String mobile;
    private String address;
    private String city;
    private String state;
    private String landmark;
    private String zip;

    public Address(String id, String name, String mobile, String address, String city, String state, String landmark, String zip) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.state = state;
        this.landmark = landmark;
        this.zip = zip;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
