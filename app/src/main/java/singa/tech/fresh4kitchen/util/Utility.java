package singa.tech.fresh4kitchen.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import singa.tech.fresh4kitchen.BuildConfig;
import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.MainActivity;


public class Utility {

    static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static ProgressDialog dialog;

    public static void toastView(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void setMainFragment(Fragment fragment, Context ctx) {
        hideKeyboard((MainActivity) ctx);
        ((MainActivity) ctx).getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_framelayout, fragment).commit();
    }

    public static void setPage(SessionManager sessionManager, String name) {
        sessionManager.setData(SessionManager.KEY_PAGE_NAME, name);
    }


    public static void loadImage(Context ctx, String url, ImageView imageView) {

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(imageView);
    }

    public static void showLoader(Context ctx) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new ProgressDialog(ctx);
        dialog.setMessage(ctx.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void hideLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static boolean emailCheck(String email) {
        String mail = email.replace(" ", "");
        if (mail.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public static String shareApptext() {
        String shareMessage = "\nDownloading the application for get fresh fruits and vegetables at your door\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        return shareMessage;
    }

    public static void shareApp(Context ctx, String app_package, String content) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Fresh4Kitchen");
            i.putExtra(Intent.EXTRA_TEXT, content);
            i.setPackage(app_package);
            ctx.startActivity(i);
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context ctx) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                ctx.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static void rateUs(Context ctx) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ctx.getPackageName()));
        ctx.startActivity(rateIntent);
    }

    public static boolean isNullOrBlank(String value) {
        if (value == null || value.equals(null) || value.equals("null") || value.equals("")) {
            return true;
        } else if (value.length() > 0) {
            return false;
        }
        return false;
    }

    public static boolean checkPassword(String pass) {

        boolean isAtLeast8 = pass.length() >= 8;//Checks for at least 8 characters
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        if (isAtLeast8) {
            for (int i = 0; i < pass.length(); i++) {
                Character character = pass.charAt(i);
                if (Character.isLowerCase(character)) {
                    upper = true;
                }
                if (Character.isUpperCase(character)) {
                    lower = true;
                }
                if (Character.isDigit(character)) {
                    digit = true;
                }
            }
            if (upper) {
                if (lower) {
                    if (digit) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getdate() {
//        DateFormat df = new SimpleDateFormat("dd-MM-yyyy h:mm a");
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getwastagedate() {
//        DateFormat df = new SimpleDateFormat("dd-MM-yyyy h:mm a");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getTodaydate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static int getCrrentMonth() {
        DateFormat df = new SimpleDateFormat("MM");
        String month = df.format(Calendar.getInstance().getTime());
        return Integer.parseInt(month);
    }

    public static void openWhatsApp(String number, String message, Activity activity) {

        try {
            PackageManager packageManager = activity.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                activity.startActivity(i);
            } else {
                toastView(activity, "error");
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            toastView(activity, e.toString());
        }

    }

    public static String getQty(boolean add, String pre_qty) {
        String new_qty = "1";
        if (!pre_qty.equals("")) {
            int qty = Integer.parseInt(pre_qty);
            if (add) {
                qty = ++qty;
            } else {
                qty = --qty;
            }
            new_qty = String.valueOf(qty);
        }
        return new_qty;
    }
}
