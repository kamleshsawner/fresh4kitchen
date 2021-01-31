package singa.tech.fresh4kitchen.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import singa.tech.fresh4kitchen.R;

public class MyDialogView {

    public static void exitDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_application);
        TextView title_tv = dialog.findViewById(R.id.tv_dialogaapp_title);
        TextView msg_tv = dialog.findViewById(R.id.tv_dialogaapp_msg);
        msg_tv.setText("Are you sure to exit from application ?");
        title_tv.setText("Exit ");
        Button yes_bt = dialog.findViewById(R.id.bt_dialogaapp_yes);
        Button no_bt = dialog.findViewById(R.id.bt_dialogaapp_no);
        yes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finishAffinity();
            }
        });
        no_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void logoutDialog(final Activity activity, final SessionManager sessionManager) {
        final Dialog dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_application);
        TextView title_tv = dialog.findViewById(R.id.tv_dialogaapp_title);
        TextView msg_tv = dialog.findViewById(R.id.tv_dialogaapp_msg);
        msg_tv.setText("Are you sure to logout from application ?");
        title_tv.setText("Logout ");
        Button yes_bt = dialog.findViewById(R.id.bt_dialogaapp_yes);
        Button no_bt = dialog.findViewById(R.id.bt_dialogaapp_no);
        yes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sessionManager.logoutUser(activity);
            }
        });
        no_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
