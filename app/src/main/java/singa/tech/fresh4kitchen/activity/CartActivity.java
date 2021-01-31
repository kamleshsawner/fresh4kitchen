package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.adapter.CartProductsAdapter;
import singa.tech.fresh4kitchen.model.Cart;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    RecyclerView recyclerView;
    Button start_bt, place_bt;
    TextView total_tv;
    ImageView back_iv;
    LinearLayout nodata_ll, data_ll;

    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getCart(ApiObject.getCart(sessionManager.getData(SessionManager.KEY_ID)));
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
    }

    private void initXml() {
        ctx = this;
        recyclerView = findViewById(R.id.rv_cart_recyclerview);
        start_bt = findViewById(R.id.bt_cart_start);
        place_bt = findViewById(R.id.bt_cart_placeorder);
        total_tv = findViewById(R.id.tv_cart_total);
        nodata_ll = findViewById(R.id.ll_cart_nodata);
        data_ll = findViewById(R.id.ll_cart_data);
        back_iv = findViewById(R.id.iv_cart_back);

        start_bt.setOnClickListener(this);
        place_bt.setOnClickListener(this);
        back_iv.setOnClickListener(this);

        sessionManager = new SessionManager(ctx);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_cart_back:
                finish();
                break;

            case R.id.bt_cart_start:
                finishAffinity();
                startActivity(new Intent(ctx, MainActivity.class));
                break;

            case R.id.bt_cart_placeorder:
                startActivity(new Intent(ctx, AddressActivity.class)
                        .putExtra("total", getTotal()));
                break;
        }
    }

    private String getTotal() {
        float tot = Float.parseFloat(total_tv.getText().toString());
        return String.format("%.0f", tot);
    }

    private void getCart(JSONObject object) {

        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.CART_API)
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Utility.hideLoader();
                        Utility.toastView(ctx, anError.toString());
                    }
                });
    }

    private void setResponse(JSONObject response) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                data_ll.setVisibility(View.VISIBLE);
                JSONArray data_array = response.getJSONArray("cart_data");
                ArrayList<Cart> product_list = JsonParser.getCartProducts(data_array);
                setAdapter(product_list);
            } else {
                data_ll.setVisibility(View.GONE);
                nodata_ll.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<Cart> product_list) {

        String userid = sessionManager.getData(SessionManager.KEY_ID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        CartProductsAdapter adapter = new CartProductsAdapter(ctx, product_list, userid);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        setTotal(product_list);
    }

    public void setTotal(ArrayList<Cart> product_list) {

        float total = 0;
        for (int i = 0; i < product_list.size(); i++) {
            int qty = Integer.parseInt(product_list.get(i).getQty());
            float price = Float.parseFloat(product_list.get(i).getPrice());
            total = total + qty * price;
        }
        sessionManager.setData(SessionManager.KEY_COUNTER, product_list.size() + "");
        total_tv.setText("" + total);
    }


}
