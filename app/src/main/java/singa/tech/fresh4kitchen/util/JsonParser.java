package singa.tech.fresh4kitchen.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.model.Cart;
import singa.tech.fresh4kitchen.model.MyOrder;
import singa.tech.fresh4kitchen.model.Notification;
import singa.tech.fresh4kitchen.model.Products;

public class JsonParser {

    public static ArrayList<String> getBannerlist(JSONArray banner_array) {
        ArrayList<String> list = new ArrayList<>();
        if (banner_array.length() > 0) {
            for (int i = 0; i < banner_array.length(); i++) {
                try {
                    JSONObject object = banner_array.getJSONObject(i);
                    String image = object.getString("banner_image");
                    list.add(image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static ArrayList<Products> getProductlist(JSONArray data_array) {
        ArrayList<Products> list = new ArrayList<>();
        if (data_array.length() > 0) {
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    JSONObject object = data_array.getJSONObject(i);
                    String id = object.getString("id");
                    String category_id = object.getString("category_id");
                    String name = object.getString("name");
                    String cover_img = object.getString("cover_img");
                    String product_img1 = object.getString("product_img1");
                    String product_img2 = object.getString("product_img2");
                    String product_img3 = object.getString("product_img3");
                    String product_img4 = object.getString("product_img4");
                    String about = object.getString("about");
                    String benifit = object.getString("benifit");
                    String featured = object.getString("featured");
                    String category_name = object.getString("category_name");
                    list.add(new Products(id, category_id, name, cover_img, product_img1, product_img2, product_img3, product_img4, about, benifit, featured, category_name, "", "", "", "", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static Products getSingleProduct(JSONObject object) {
        Products products = null;
        try {
            String id = object.getString("id");
            String category_id = object.getString("category_id");
            String name = object.getString("name");
            String product_img1 = object.getString("product_img1");
            String product_img2 = object.getString("product_img2");
            String product_img3 = object.getString("product_img3");
            String product_img4 = object.getString("product_img4");
            String about = object.getString("about");
            String benifit = object.getString("benifit");
            String category_name = object.getString("category_name");
            String verient_id = object.getString("varient_id");
            String unit = object.getString("unit_name");
            String percenatge = object.getString("percentage");
            String price = object.getString("price");
            String offe_price = object.getString("offer_price");
            products = new Products(id, category_id, name, "", product_img1, product_img2, product_img3, product_img4, about, benifit, "", category_name, unit, price, offe_price, percenatge, verient_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static ArrayList<Products> getCateProductlist(JSONArray data_array, ArrayList<Products> product_list) {
        if (data_array.length() > 0) {
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    JSONObject object = data_array.getJSONObject(i);
                    String id = object.getString("id");
                    String category_id = object.getString("category_id");
                    String name = object.getString("name");
                    String cover_img = object.getString("cover_img");
                    String unit = object.getString("unit_name");
                    String actual_price = object.getString("price");
                    String offer_price = object.getString("offer_price");
                    String percentage = object.getString("percentage");
                    String verient_id = object.getString("varient_id");
                    product_list.add(new Products(id, category_id, name, cover_img, "", "", "", "", "", "", "", "", unit, actual_price, offer_price, percentage, verient_id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return product_list;
    }

    // my order
    public static ArrayList<MyOrder> getMyOrder(JSONArray data_array) {
        ArrayList<MyOrder> list = new ArrayList<>();
        if (data_array.length() > 0) {
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    JSONObject object = data_array.getJSONObject(i);
                    String id = object.getString("id");
                    String order_id = object.getString("order_id");
                    String name = object.getString("name");
                    String price = object.getString("price");
                    String cover_img = object.getString("cover_img");
                    String unit = object.getString("varient_qty");
                    String status = object.getString("status");
                    String qty = object.getString("qty");
                    String created_date = object.getString("created_date_time");
                    if (created_date.length() > 18) {
                        String date = created_date.substring(0, 11);
                        String time = created_date.substring(12, 17);
                        list.add(new MyOrder(id, order_id, name, price, qty, unit, cover_img, date, time, status));
                    } else {
                        list.add(new MyOrder(id, order_id, name, price, qty, unit, cover_img, "", "", status));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static ArrayList<Cart> getCartProducts(JSONArray data_array) {
        ArrayList<Cart> list = new ArrayList<>();
        if (data_array.length() > 0) {
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    JSONObject object = data_array.getJSONObject(i);
                    String id = object.getString("id");
                    String user_id = object.getString("user_id");
                    String product_id = object.getString("product_id");
                    String verient_id = object.getString("varient_id");
                    String unit = object.getString("varient_qty");
                    String qty = object.getString("qty");
                    String price = object.getString("price");
                    String name = object.getString("name");
                    String cover_img = object.getString("cover_img");
                    list.add(new Cart(id, user_id, product_id, verient_id, unit, qty, price, name, cover_img));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static ArrayList<Notification> getNotification(JSONArray data_array) {
        ArrayList<Notification> list = new ArrayList<>();
        if (data_array.length() > 0) {
            for (int i = 0; i < data_array.length(); i++) {
                try {
                    JSONObject object = data_array.getJSONObject(i);
                    String notification = object.getString("notification");
                    String created_date_time = object.getString("created_date_time").substring(0,11);
                    list.add(new Notification(notification, created_date_time));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
