package singa.tech.fresh4kitchen.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

@SuppressLint("ValidFragment")
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    TextView refer_code_tv, mobile_et;
    EditText name_et, address_et, email_et, pin_et;
    ImageView back_iv;
    Button upadte_bt;
    CircleImageView circleImageView;

    SessionManager sessionManager;

    //profile image
    String TAG = "profile Image Upload";
    File file;
    String filename = "";
    public static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getProfile(ApiObject.getProfile(sessionManager.getData(SessionManager.KEY_ID)));
        }
    }

    private void initXml() {

        ctx = this;
        back_iv = findViewById(R.id.iv_profile_back);
        name_et = findViewById(R.id.et_profile_name);
        mobile_et = findViewById(R.id.et_profile_mobile);
        address_et = findViewById(R.id.et_profile_address);
        email_et = findViewById(R.id.et_profile_email);
        pin_et = findViewById(R.id.et_profile_picode);
        refer_code_tv = findViewById(R.id.tv_profile_refercode);
        upadte_bt = findViewById(R.id.bt_profile_update);
        circleImageView = findViewById(R.id.iv_profile_image);

        back_iv.setOnClickListener(this);
        upadte_bt.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        sessionManager = new SessionManager(ctx);
        String image = sessionManager.getData(SessionManager.KEY_USER_IMAGE);
        if (!Utility.isNullOrBlank(image)) {
            Utility.loadImage(ctx, image, circleImageView);
        } else {
            circleImageView.setImageResource(R.drawable.profile);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_profile_update:
                if (ConnectionDetector.isConnected()) {
                    updateProfile(ApiObject.updateProfile(sessionManager.getData(SessionManager.KEY_ID),
                            name_et.getText().toString(),
                            email_et.getText().toString(),
                            address_et.getText().toString(),
                            pin_et.getText().toString()));
                }
                break;

            case R.id.iv_profile_image:
                requestStoragePermission();
                break;

            case R.id.iv_profile_back:
                finish();
                break;

        }
    }

    // update profile
    private void updateProfile(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.PROFILE_API)
                .addJSONObjectBody(object)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setupdateResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setupdateResponse(JSONObject response) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                sessionManager.setData(SessionManager.KEY_NAME, name_et.getText().toString());
                sessionManager.setData(SessionManager.KEY_MOBILE, mobile_et.getText().toString());
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // get profile
    private void getProfile(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.PROFILE_API)
                .addJSONObjectBody(object)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setResponse(JSONObject response) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                JSONObject data_obj = response.getJSONObject("data");
                String name = data_obj.getString("name");
                String mobile = data_obj.getString("mobile_no");
                String email = data_obj.getString("google_id");
                String adres = data_obj.getString("address");
                String pin = data_obj.getString("pin_code");
                String refer = data_obj.getString("refer_code");
                setData(name, mobile, email, adres, pin, refer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(String name, String mobile, String email, String adres, String pin, String refer) {

        name_et.setText(name);
        mobile_et.setText(mobile);
        email_et.setText(email);
        address_et.setText(adres);
        pin_et.setText(pin);
        refer_code_tv.setText(refer);

        sessionManager.setData(SessionManager.KEY_NAME, name);
        sessionManager.setData(SessionManager.KEY_MOBILE, mobile);
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            browseImage();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
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
                        Utility.toastView(ctx, "Error occurred! ");
                    }
                })
                .onSameThread()
                .check();
    }

    private void browseImage() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, PICK_IMAGE_REQUEST);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ctx.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String filePath = getRealPathFromURIPath(uri);
            file = new File(filePath);
            filename = file.getName();
            Log.d(TAG, "Filename " + file.getName());

            Bitmap bmp = null;
            try {
                bmp = Utility.getBitmapFromUri(uri, ctx);
                circleImageView.setImageBitmap(bmp);
                uploadImage();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        String userid = sessionManager.getData(SessionManager.KEY_ID);
        Utility.showLoader(ctx);
        AndroidNetworking.upload(Webapi.PICTURE_UPLOAD_API)
                .addMultipartFile("file", getCompressFile(file))
                .addMultipartParameter("user_id", userid)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        try {
                            JSONObject info_obj = response.getJSONObject("info");
                            String status = info_obj.getString("status");
                            String msg = info_obj.getString("message");
                            if (status.equals("200")) {
                                String image = response.getString("data");
                                sessionManager.setData(SessionManager.KEY_USER_IMAGE, image);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                        Utility.toastView(ctx, error.toString());
                    }
                });
    }

    private File getCompressFile(File file) {
        File compressedImage = null;
        try {
            compressedImage = new Compressor(ctx)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedImage;
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        Cursor cursor = ctx.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
