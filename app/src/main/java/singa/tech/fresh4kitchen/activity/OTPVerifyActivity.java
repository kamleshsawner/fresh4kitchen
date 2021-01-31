package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

import static singa.tech.fresh4kitchen.util.Constant.OTP_REQUEST_RESEND;
import static singa.tech.fresh4kitchen.util.Constant.OTP_REQUEST_VERIFY;

public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    EditText otp_et;
    Button submit_bt;
    TextView resend_tv;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        initXml();
    }

    private void initXml() {
        ctx = this;
        otp_et = findViewById(R.id.et_otp_otp);
        submit_bt = findViewById(R.id.bt_otp_submit);
        resend_tv = findViewById(R.id.tv_otp_resend);

        submit_bt.setOnClickListener(this);
        resend_tv.setOnClickListener(this);

        sessionManager = new SessionManager(ctx);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_otp_submit:
                if (isValid()) {
                    getRequest(ApiObject.getOTPVerify(getIntent().getStringExtra("mobile")
                            , otp_et.getText().toString()), OTP_REQUEST_VERIFY);
                }
                break;

            case R.id.tv_otp_resend:
                if (ConnectionDetector.isConnected()) {
                    getRequest(ApiObject.getOTP("1", getIntent().getStringExtra("mobile")), OTP_REQUEST_RESEND);
                } else {
                    Utility.toastView(ctx, ctx.getString(R.string.no_internet));
                }
                break;
        }

    }

    private boolean isValid() {
        if (!ConnectionDetector.isConnected()) {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
            return false;
        }
        if (otp_et.getText().toString().equals("")) {
            Utility.toastView(ctx, "Please enter OTP");
            return false;
        }
        return true;
    }

    private void getRequest(JSONObject object, final int type) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.LOGIN_API)
                .addJSONObjectBody(object)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setResponse(response, type);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setResponse(JSONObject response, int type) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                if (type == OTP_REQUEST_VERIFY) {
                    JSONObject data_obj = response.getJSONObject("data");
                    String id = data_obj.getString("id");
                    String mobile_no = data_obj.getString("mobile_no");
                    String name = data_obj.getString("name");
                    String address = data_obj.getString("address");
                    String refer_code = data_obj.getString("refer_code");
                    String user_img = data_obj.getString("user_image");
                    sessionManager.createLoginSession(name, mobile_no, address, id, refer_code, user_img);
                    finishAffinity();
                    startActivity(new Intent(ctx, RegisterSuccessfullyActivity.class));
                    clear();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        otp_et.setText("");
    }


}
