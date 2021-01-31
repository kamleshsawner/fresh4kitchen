package singa.tech.fresh4kitchen.model;

public class Notification {

    private String message;
    private String date;

    public Notification(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
