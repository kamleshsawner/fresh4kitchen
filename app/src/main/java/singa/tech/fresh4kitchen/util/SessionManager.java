package singa.tech.fresh4kitchen.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.app.ActivityCompat;

import singa.tech.fresh4kitchen.activity.LoginActivity;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context ctx;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sessionmanager";
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_REFER_CODE = "refercode";
    public static final String KEY_PAGE_NAME = "pagename";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_COUNTER = "counter";

    public SessionManager(Context context) {
        this.ctx = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String mobile, String address, String userid, String refercode, String user_img) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, userid);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_REFER_CODE, refercode);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_USER_IMAGE, user_img);
        editor.commit();
    }

    public void setData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void logoutUser(Activity mainActivity) {
        editor.clear();
        editor.commit();
        ActivityCompat.finishAffinity(mainActivity);
        Intent i = new Intent(ctx.getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getData(String key) {
        return pref.getString(key, null);
    }
}
//82098252
