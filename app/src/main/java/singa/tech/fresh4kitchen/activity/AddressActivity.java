package singa.tech.fresh4kitchen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.adapter.AdapterAddress;
import singa.tech.fresh4kitchen.model.Address;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;


public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    Context ctx;
    EditText name_et, mobile_et, address_et, city_et, state_et, zipcode_et, landmark_et;
    Button submit_bt, cancel_bt;
    TextView add_tv, select_tv;
    ImageView back_iv;
    LinearLayout address_ll;
    RecyclerView recyclerView;

    SessionManager sessionManager;
    ArrayList<Address> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getAddress(ApiObject.getAddress(sessionManager.getData(SessionManager.KEY_ID)));
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
    }

    private void initXml() {

        ctx = this;
        add_tv = findViewById(R.id.tv_address_add);
        select_tv = findViewById(R.id.tv_address_select);
        submit_bt = findViewById(R.id.bt_address_submit);
        cancel_bt = findViewById(R.id.bt_address_cancel);
        name_et = findViewById(R.id.et_address_name);
        mobile_et = findViewById(R.id.et_address_mobile);
        address_et = findViewById(R.id.et_address_address);
        city_et = findViewById(R.id.et_address_city);
        state_et = findViewById(R.id.et_address_state);
        zipcode_et = findViewById(R.id.et_address_pin);
        landmark_et = findViewById(R.id.et_address_land);
        back_iv = findViewById(R.id.iv_address_back);
        recyclerView = findViewById(R.id.rv_address_recyclerview);
        address_ll = findViewById(R.id.ll_address_new);

        add_tv.setOnClickListener(this);
        select_tv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        cancel_bt.setOnClickListener(this);
        back_iv.setOnClickListener(this);

        list = new ArrayList<>();
        sessionManager = new SessionManager(ctx);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_address_back:
                finish();
                break;
            case R.id.tv_address_add:
                setSelected(false);
                recyclerView.setVisibility(View.GONE);
                address_ll.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_address_select:
                if (list.size() > 0) {
                    setSelected(true);
                    recyclerView.setVisibility(View.VISIBLE);
                    address_ll.setVisibility(View.GONE);
                } else {
                    Utility.toastView(ctx, "Not Available");
                }
                break;
            case R.id.bt_address_cancel:
                finish();
                break;

            case R.id.bt_address_submit:
                if (isValid()) {
                    if (ConnectionDetector.isConnected()) {
                        submitAddress(ApiObject.addAddress(sessionManager.getData(SessionManager.KEY_ID)
                                , name_et.getText().toString()
                                , mobile_et.getText().toString()
                                , landmark_et.getText().toString()
                                , address_et.getText().toString()
                                , city_et.getText().toString()
                                , zipcode_et.getText().toString()
                                , state_et.getText().toString()));
                    } else {
                        Utility.toastView(ctx, ctx.getString(R.string.no_internet));
                    }
                }
                break;
        }
    }

    private boolean isValid() {
        boolean response = true;
        if (name_et.getText().toString().equals("")) {
            name_et.setError("Pleas enter name");
            response = false;
        }
        if (mobile_et.getText().toString().equals("")) {
            mobile_et.setError("Pleas enter mobile number");
            response = false;
        }
        if (address_et.getText().toString().equals("")) {
            address_et.setError("Pleas enter address");
            response = false;
        }
        if (city_et.getText().toString().equals("")) {
            city_et.setError("Pleas enter city");
            response = false;
        }
        if (state_et.getText().toString().equals("")) {
            state_et.setError("Pleas enter state");
            response = false;
        }
        if (zipcode_et.getText().toString().equals("")) {
            zipcode_et.setError("Pleas enter pincode");
            response = false;
        }
        if (landmark_et.getText().toString().equals("")) {
            landmark_et.setError("Pleas enter landmark");
            response = false;
        }
        return response;
    }

    private void getAddress(JSONObject object) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.ADDRESS_API)
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
        list = new ArrayList<>();
        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                JSONArray data_array = response.getJSONArray("data");
                if (data_array.length() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    setSelected(true);
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject jsonObject = data_array.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("user_name");
                        String mobile = jsonObject.getString("mobile_no");
                        String address = jsonObject.getString("address");
                        String city = jsonObject.getString("city");
                        String state = jsonObject.getString("state");
                        String landmark = jsonObject.getString("landmark");
                        String pincode = jsonObject.getString("pincode");
                        list.add(new Address(id, name, mobile, address, city, state, landmark, pincode));
                    }
                    Collections.reverse(list);
                    setAdapter(list);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                address_ll.setVisibility(View.VISIBLE);
                setSelected(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSelected(boolean select) {

        if (select) {
            select_tv.setTypeface(null, Typeface.BOLD);
            add_tv.setTypeface(null, Typeface.NORMAL);
        } else {
            add_tv.setTypeface(null, Typeface.BOLD);
            select_tv.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setAdapter(ArrayList<Address> list) {
        String total = getIntent().getStringExtra("total");
        AdapterAddress adapter = new AdapterAddress(ctx, list,total);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void submitAddress(JSONObject object) {

        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.ADDRESS_API)
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
                    public void onError(ANError error) {
                        // handle error
                        Utility.hideLoader();
                        Utility.toastView(ctx, error.toString());
                    }
                });
    }

    private void setResponse(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("info");
            String status = object.getString("status");
            String message = object.getString("message");
            if (status.equals("200")) {
                getAddress(ApiObject.getAddress(sessionManager.getData(SessionManager.KEY_ID)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selectAddress(String id, String address) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("id", id);
        returnIntent.putExtra("data", address);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
