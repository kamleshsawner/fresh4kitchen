package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    EditText mobile_et;
    Button login_bt;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initXml();
        checkLogin();
    }

    private void checkLogin() {
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(ctx, MainActivity.class));
            finish();
        }
    }

    private void initXml() {
        ctx = this;
        mobile_et = findViewById(R.id.et_login_mobile);
        login_bt = findViewById(R.id.bt_login_submit);
        login_bt.setOnClickListener(this);

        sessionManager = new SessionManager(ctx);

        mobile_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mobile_et.getText().toString().length() == 10) {
                    mobile_et.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(ctx, R.drawable.right_icon), null);
                } else {
                    mobile_et.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_login_submit:
                if (isValid()) {
                    userLogin(ApiObject.getOTP("0", mobile_et.getText().toString()));
                }
                break;
        }
    }

    private boolean isValid() {
        if (!ConnectionDetector.isConnected()) {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
            return false;
        }
        if (mobile_et.getText().toString().length() != 10) {
            Utility.toastView(ctx, "Invalid mobile number");
            return false;
        }
        return true;
    }

    private void userLogin(JSONObject object) {
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
                startActivity(new Intent(ctx, OTPVerifyActivity.class)
                        .putExtra("mobile", mobile_et.getText().toString()));
                clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        mobile_et.setText("");
    }


}
