package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.adapter.AdapterCoupon;
import singa.tech.fresh4kitchen.model.Coupon;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;


public class CouponActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    ImageView back_iv;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getCoupons(ApiObject.getCoupon());
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
    }

    private void initXml() {

        ctx = this;
        back_iv = findViewById(R.id.iv_coupon_back);
        recyclerView = findViewById(R.id.rv_coupon_recyclerview);

        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_coupon_back:
                finish();
                break;
        }
    }


    private void getCoupons(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.COUPON_API)
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setListResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Utility.hideLoader();
                        Utility.toastView(ctx, error.toString());
                    }
                });
    }

    private void setListResponse(JSONObject response) {
        ArrayList<Coupon> list = new ArrayList<>();
        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                JSONObject jsonObject = response.getJSONObject("data");
                String id = jsonObject.getString("id");
                String code = jsonObject.getString("coupon_code");
                String discount = jsonObject.getString("off");
                String date = jsonObject.getString("created_date_time");
                list.add(new Coupon(id, "", code, discount, date));
                setAdapter(list);
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    private void setAdapter(ArrayList<Coupon> list) {
        AdapterCoupon adapter = new AdapterCoupon(ctx, list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
