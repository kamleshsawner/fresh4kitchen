package singa.tech.fresh4kitchen.util;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiObject {

    public static JSONObject getOTP(String mode, String mobile) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", mode);
            object.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getOTPVerify(String mobile, String otp) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "2");
            object.put("mobile", mobile);
            object.put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getHome(String mode, String userid) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", mode);
            object.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getCategoryProduct(String mode, String limit) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", mode);
            object.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getProductdetail(String id) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "3");
            object.put("product_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getAddcart(String product_id, String userid, String price, String verient_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "0");
            object.put("product_id", product_id);
            object.put("user_id", userid);
            object.put("qty", "1");
            object.put("price", price);
            object.put("varient_id", verient_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getUpdatecart(String qty, String userid, String verient_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "1");
            object.put("user_id", userid);
            object.put("qty", qty);
            object.put("varient_id", verient_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getRemovecart(String userid, String verient_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "2");
            object.put("user_id", userid);
            object.put("varient_id", verient_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getCart(String userid) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "3");
            object.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // Get profile
    public static JSONObject getProfile(String userid) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "0");
            object.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // Update profile
    public static JSONObject updateProfile(String userid, String name, String email, String address, String pin) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "1");
            object.put("user_id", userid);
            object.put("name", name);
            object.put("email_id", email);
            object.put("address", address);
            object.put("pincode", pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // get address
    public static JSONObject getAddress(String userid) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "0");
            object.put("user_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // get coupon
    public static JSONObject getCoupon() {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // add address
    public static JSONObject addAddress(String userid, String name, String mobile, String landmark, String address, String city, String pin, String state) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "1");
            object.put("user_id", userid);
            object.put("user_name", name);
            object.put("mobile_no", mobile);
            object.put("landmark", landmark);
            object.put("address", address);
            object.put("city", city);
            object.put("state", state);
            object.put("pincode", pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // delete address
    public static JSONObject deleteAddress(String id) {
        JSONObject object = new JSONObject();
        try {
            object.put("mode", "3");
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // submit order
    public static JSONObject submitOrder(String user_id, String address_id, String total, String pay_method, String txn_id, String time, String coupon_code) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
            object.put("address_id", address_id);
            object.put("total_amount", total);
            object.put("pay_method", pay_method);
            object.put("txn_id", txn_id);
            object.put("time", time);
            object.put("coupan_code", coupon_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    // my orders
    public static JSONObject myOrder(String user_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject getSearching(String text) {
        JSONObject object = new JSONObject();
        try {
            object.put("product_name", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
