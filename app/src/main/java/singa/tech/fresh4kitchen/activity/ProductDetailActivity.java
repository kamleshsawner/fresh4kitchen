package singa.tech.fresh4kitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.adapter.AdapterMainProductDetail;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    ImageView back_iv, plus_iv, minus_iv;
    TextView qty_tv, title_tv, counte_tv;
    Button add_bt;
    RecyclerView recyclerView;
    SessionManager sessionManager;
    RelativeLayout cart_ll;


    String userid = "", product_id = "", verient_id = "", price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdeatils);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getDetails(ApiObject.getProductdetail(getIntent().getStringExtra("id")));
        }
    }

    private void initXml() {
        ctx = this;
        back_iv = findViewById(R.id.iv_proddetail_back);
        plus_iv = findViewById(R.id.iv_proddetail_add);
        minus_iv = findViewById(R.id.iv_proddetail_remove);
        recyclerView = findViewById(R.id.rv_prodetail_recyclerview);
        qty_tv = findViewById(R.id.tv_proddetail_qty);
        title_tv = findViewById(R.id.tv_prodetail_head);
        add_bt = findViewById(R.id.bt_productdetail_add);
        counte_tv = findViewById(R.id.tv_main_counter);
        cart_ll = findViewById(R.id.rl_main_cart);

        sessionManager = new SessionManager(ctx);

        back_iv.setOnClickListener(this);
        plus_iv.setOnClickListener(this);
        minus_iv.setOnClickListener(this);
        cart_ll.setOnClickListener(this);
        add_bt.setOnClickListener(this);
        setCounter();
    }

    private void getDetails(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.CATEGORY_API)
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
                JSONObject data_obj = response.getJSONObject("single_product_data");
                Products products = JsonParser.getSingleProduct(data_obj);

                title_tv.setText(products.getName());
                LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
                AdapterMainProductDetail adapter = new AdapterMainProductDetail(ctx, products);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setValue(String price, String verient_id, String product_id) {

        userid = sessionManager.getData(SessionManager.KEY_ID);
        this.price = price;
        this.verient_id = verient_id;
        this.product_id = product_id;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_productdetail_add:
                if (isSelected()) {
                    addTocart(ApiObject.getAddcart(product_id, userid, price, verient_id));
                }
                break;

            case R.id.rl_main_cart:
                startActivity(new Intent(ctx, CartActivity.class));
                break;

            case R.id.iv_proddetail_add:
                break;

            case R.id.iv_proddetail_remove:
                break;

            case R.id.iv_proddetail_back:
                finish();
                break;
        }

    }

    private boolean isSelected() {
        if (verient_id.equals("")) {
            Utility.toastView(ctx, "Please select product weight");
            return false;
        } else {
            return true;
        }
    }

    private String getQuantity(boolean add) {
        int qty = Integer.parseInt(qty_tv.getText().toString());
        if (add) {
            qty = ++qty;
        } else {
            qty = --qty;
        }
        return String.valueOf(qty);
    }

    // add to cart
    private void addTocart(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.CART_API)
                .addJSONObjectBody(object)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setaddResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setaddResponse(JSONObject response) {
        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (msg.equals("Success!")) {
                String counter = response.getString("data");
                sessionManager.setData(SessionManager.KEY_COUNTER, counter);
                Utility.toastView(ctx, "Added to cart");
                setCounter();
            } else {
                if (status.equals("200")) {
                    Utility.toastView(ctx, "Already available in your cart");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCounter() {
        if (Utility.isNullOrBlank(sessionManager.getData(SessionManager.KEY_COUNTER))) {
            counte_tv.setText("0");
        } else {
            counte_tv.setText(sessionManager.getData(SessionManager.KEY_COUNTER));
        }
    }
}
