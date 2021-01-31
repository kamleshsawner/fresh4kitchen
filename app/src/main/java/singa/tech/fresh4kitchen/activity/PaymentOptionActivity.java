package singa.tech.fresh4kitchen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.Constant;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

public class PaymentOptionActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    RadioButton cod_rb, netbanking_rb, card_rb;
    Button submit_bt;
    ImageView back_iv;

    SessionManager sessionManager;
    String user_id = "", address_id = "", total = "", timeslot = "", coupon_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        initXml();
    }

    private void initXml() {
        ctx = this;
        cod_rb = findViewById(R.id.rb_pay_cod);
        netbanking_rb = findViewById(R.id.rb_pay_netbank);
        card_rb = findViewById(R.id.rb_pay_card);
        submit_bt = findViewById(R.id.bt_pay_submit);
        back_iv = findViewById(R.id.iv_pay_back);

        submit_bt.setOnClickListener(this);
        back_iv.setOnClickListener(this);

        sessionManager = new SessionManager(ctx);
        user_id = sessionManager.getData(SessionManager.KEY_ID);
        address_id = getIntent().getStringExtra("id");
        total = getIntent().getStringExtra("total");
        timeslot = getIntent().getStringExtra("time");
        coupon_code = getIntent().getStringExtra("coupon");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_pay_back:
                finish();
                break;

            case R.id.bt_pay_submit:
                if (ConnectionDetector.isConnected()) {
                    if (ispaymentCod()) {
                        submitOrder(ApiObject.submitOrder(user_id, address_id, total, "C.O.D.", "", timeslot, coupon_code));
                    } else {
                        startActivityForResult(new Intent(ctx, RazorPayActivity.class)
                                .putExtra("amount", total), Constant.INTENT_PAYMENT);
                    }
                }
                break;
        }
    }

    private String getOrderId() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        return "order_" + datetime;
    }

    private boolean ispaymentCod() {
        if (cod_rb.isChecked()) {
            return true;
        } else {
            return false;

        }
    }

    // submit order0
    private void submitOrder(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.SUBMIT_ORDER_API)
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
                Utility.toastView(ctx, "Order placed successfully");
                finishAffinity();
                startActivity(new Intent(ctx, MainActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.INTENT_PAYMENT && resultCode == Activity.RESULT_OK) {
            String txn_id = data.getStringExtra("id");
            submitOrder(ApiObject.submitOrder(user_id, address_id, total, "RazorPay", txn_id, timeslot, coupon_code));
        }
    }
}
