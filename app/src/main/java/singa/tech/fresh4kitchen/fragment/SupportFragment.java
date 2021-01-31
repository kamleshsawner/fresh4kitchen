package singa.tech.fresh4kitchen.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.Utility;

@SuppressLint("ValidFragment")
public class SupportFragment extends Fragment implements View.OnClickListener {

    Context ctx;
    View view = null;
    TextView email_tv, call_tv;
    ImageView whatsap_iv, fb_iv, linkedin_iv, insta_iv, twitter_iv;

    @SuppressLint("ValidFragment")
    public SupportFragment(Context ctx) {
        this.ctx = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_support, null);
        initXml();
        return view;
    }

    private void initXml() {
        email_tv = view.findViewById(R.id.tv_suppport_email);
        call_tv = view.findViewById(R.id.tv_suppport_call);

        whatsap_iv = view.findViewById(R.id.iv_refer_whatsapp);
        fb_iv = view.findViewById(R.id.iv_refer_facebook);
        linkedin_iv = view.findViewById(R.id.iv_refer_linked);
        insta_iv = view.findViewById(R.id.iv_refer_insta);
        twitter_iv = view.findViewById(R.id.iv_refer_twitter);

        whatsap_iv.setOnClickListener(this);
        fb_iv.setOnClickListener(this);
        linkedin_iv.setOnClickListener(this);
        insta_iv.setOnClickListener(this);
        twitter_iv.setOnClickListener(this);
        email_tv.setOnClickListener(this);
        call_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_refer_facebook:
                facebookRefer(Utility.shareApptext());
                break;

            case R.id.iv_refer_whatsapp:
                WhatsappRefer(Utility.shareApptext());
                break;

            case R.id.iv_refer_insta:
                InstaRefer(Utility.shareApptext());
                break;

            case R.id.iv_refer_linked:
                linkedinRefer(Utility.shareApptext());
                break;

            case R.id.iv_refer_twitter:
                twitterRefer(Utility.shareApptext());
                break;

            case R.id.tv_suppport_email:
                getEmail(email_tv);
                break;

            case R.id.tv_suppport_call:
                calling(call_tv);
                break;
        }
    }

    private void getEmail(TextView textView) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", textView.getText().toString(), null));
        ctx.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private void calling(final TextView textView) {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            ctx.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textView.getText().toString())));
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            Utility.toastView(ctx, "Permission is needed , Please go to Setting -> Application -> Allow Phone Call");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(ctx, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void WhatsappRefer(String shareApptext) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareApptext);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Utility.toastView(ctx, "Whatsapp have not been installed.");
        }
    }

    private void InstaRefer(String shareApptext) {
        Utility.shareApp(ctx, "com.instagram.android", Utility.shareApptext());
    }

    private void linkedinRefer(String shareApptext) {

        Intent linkedinIntent = new Intent(Intent.ACTION_SEND);
        linkedinIntent.setType("text/plain");
        linkedinIntent.putExtra(Intent.EXTRA_TEXT, shareApptext);

        boolean linkedinAppFound = false;
        List<ResolveInfo> matches2 = ctx.getPackageManager()
                .queryIntentActivities(linkedinIntent, 0);

        for (ResolveInfo info : matches2) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(
                    "com.linkedin")) {
                linkedinIntent.setPackage(info.activityInfo.packageName);
                linkedinAppFound = true;
                break;
            }
        }

        if (linkedinAppFound) {
            startActivity(linkedinIntent);
        } else {
            Utility.toastView(ctx, "LinkedIn app not Insatlled in your mobile");
        }
    }

    private void twitterRefer(String shareApptext) {
        Utility.shareApp(ctx, "com.twitter.android", Utility.shareApptext());
    }

    private void facebookRefer(String shareApptext) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareApptext);
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
/*
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                getContext().startActivity(shareIntent);
*/

                Utility.shareApp(ctx, activity.applicationInfo.packageName, Utility.shareApptext());

                break;
            }
        }
    }
}
