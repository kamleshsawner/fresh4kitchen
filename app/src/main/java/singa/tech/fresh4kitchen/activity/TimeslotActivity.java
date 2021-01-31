package singa.tech.fresh4kitchen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.util.Constant;
import singa.tech.fresh4kitchen.util.SessionManager;

public class TimeslotActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    ImageView back_iv;
    RadioButton first_rb, second_rb, third_rb;
    Button submit_bt;
    TextView apply_tv, cartamount_tv, discount_tv, total_tv, applied_tv, remove_tv, delivery_tv;
    LinearLayout promo_ll;

    SessionManager sessionManager;
    String user_id = "", address_id = "", total = "", coupon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeslot);
        initXml();
    }

    private void initXml() {
        ctx = this;
        back_iv = findViewById(R.id.iv_timeslot_back);
        first_rb = findViewById(R.id.rb_timeslot_first);
        second_rb = findViewById(R.id.rb_timeslot_second);
        third_rb = findViewById(R.id.rb_timeslot_third);
        submit_bt = findViewById(R.id.bt_timeslot_submit);
        apply_tv = findViewById(R.id.tv_checkout_apply);
        cartamount_tv = findViewById(R.id.tv_checkout_cartamount);
        discount_tv = findViewById(R.id.tv_checkout_discount);
        delivery_tv = findViewById(R.id.tv_checkout_delivery);
        total_tv = findViewById(R.id.tv_checkout_total);
        applied_tv = findViewById(R.id.tv_checkout_applysuces);
        remove_tv = findViewById(R.id.tv_checkout_remove);
        promo_ll = findViewById(R.id.ll_checkout_promocode);

        back_iv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        apply_tv.setOnClickListener(this);
        remove_tv.setOnClickListener(this);

        sessionManager = new SessionManager(ctx);
        user_id = sessionManager.getData(SessionManager.KEY_ID);
        address_id = getIntent().getStringExtra("id");
        total = getIntent().getStringExtra("total");

        setTotal("00");
    }

    private void setTotal(String discount) {
        cartamount_tv.setText(total);
        discount_tv.setText(calculateDiscount(discount));
        total_tv.setText(getDiscontedTotal());
    }

    private String calculateDiscount(String discount) {

        int per = Integer.parseInt(discount);
        float tot = Float.parseFloat(total);
        float dis = tot * per / 100;
        return String.valueOf(dis);
    }

    private String getDiscontedTotal() {

        float cart_tot = Float.parseFloat(cartamount_tv.getText().toString());
        float dis = Float.parseFloat(discount_tv.getText().toString());
        float final_tot = cart_tot - dis;
        if (final_tot >= 200) {
            delivery_tv.setText("00");
        } else {
            delivery_tv.setText("40");
        }
        final_tot = final_tot + Float.parseFloat(delivery_tv.getText().toString());
        return String.valueOf(final_tot);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_timeslot_back:
                finish();
                break;

            case R.id.tv_checkout_apply:
                startActivityForResult(new Intent(ctx, CouponActivity.class), Constant.COUPON_INTENT_REQUEST);
                break;

            case R.id.tv_checkout_remove:
                promo_ll.setVisibility(View.GONE);
                setTotal("00");
                break;

            case R.id.bt_timeslot_submit:
                ctx.startActivity(new Intent(ctx, PaymentOptionActivity.class)
                        .putExtra("id", address_id)
                        .putExtra("time", getTime())
                        .putExtra("coupon", coupon)
                        .putExtra("total", getTotal()));
                break;
        }
    }

    private String getTotal() {
        float tot = Float.parseFloat(total_tv.getText().toString());
        return String.format("%.0f", tot);
    }

    private String getTime() {
        String time = "";
        if (first_rb.isChecked()) {
            time = first_rb.getText().toString();
        } else if (second_rb.isChecked()) {
            time = second_rb.getText().toString();
        } else {
            time = third_rb.getText().toString();
        }
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.COUPON_INTENT_REQUEST && resultCode == Activity.RESULT_OK) {
            promo_ll.setVisibility(View.VISIBLE);
            coupon = data.getStringExtra("code");
            String off = data.getStringExtra("dis");
            applied_tv.setText(coupon + "\nPromocode applied successfully done");
            setTotal(off);
        }
    }
}
