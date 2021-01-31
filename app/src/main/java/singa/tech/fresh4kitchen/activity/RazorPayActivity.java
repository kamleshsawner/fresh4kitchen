package singa.tech.fresh4kitchen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.SessionManager;

public class RazorPayActivity extends AppCompatActivity implements PaymentResultListener {
    Context ctx;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);
        Checkout.preload(getApplicationContext());
        initXml();
    }

    private void initXml() {
        ctx = this;
        sessionManager = new SessionManager(ctx);
        startPayment();
    }

    private void startPayment() {
        String amount = getIntent().getStringExtra("amount") + "00";
        String name = sessionManager.getData(SessionManager.KEY_NAME);

        Checkout co = new Checkout();
        co.setKeyID("rzp_live_zEd9QhapuoarXp");
        Activity activity = this;

        JSONObject options = new JSONObject();
        try {
            options.put("name", name);
            options.put("description", "New Order");
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            co.open(activity, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        setResult(Activity.RESULT_OK, new Intent().putExtra("id", s));
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "payment Cancelled", Toast.LENGTH_LONG).show();
        finish();
    }
}
