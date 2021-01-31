package singa.tech.fresh4kitchen.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.MainActivity;
import singa.tech.fresh4kitchen.adapter.AdapterMainHome;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    Context ctx;
    RecyclerView recyclerView;
    ImageView nodata_iv;
    View view = null;

    SessionManager sessionManager;

    @SuppressLint("ValidFragment")
    public HomeFragment(Context ctx) {
        this.ctx = ctx;
        sessionManager = new SessionManager(ctx);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_recycler, null);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getHome(ApiObject.getHome("0", sessionManager.getData(SessionManager.KEY_ID)));
        }
        return view;
    }

    private void initXml() {
        recyclerView = view.findViewById(R.id.rv_layout_recyclerview);
        nodata_iv = view.findViewById(R.id.iv_layout_nodata);

        recyclerView.setVisibility(View.VISIBLE);
        nodata_iv.setVisibility(View.GONE);
    }

    private void getHome(JSONObject object) {

        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.HOME_API)
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
                JSONObject data_obj = response.getJSONObject("data");
                JSONArray banner_array = data_obj.getJSONArray("banner");
                JSONArray data_array = data_obj.getJSONArray("product_details");
                JSONObject user_object = data_obj.getJSONObject("user_details");
                String cart_counter = data_obj.getString("cart_id_count");

                ArrayList<String> banner_list = JsonParser.getBannerlist(banner_array);
                ArrayList<Products> product_list = JsonParser.getProductlist(data_array);

                sessionManager.setData(SessionManager.KEY_COUNTER,cart_counter);
                ((MainActivity) ctx).setCounter();
                setAdapter(banner_list, product_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<String> banner_list, ArrayList<Products> product_list) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        AdapterMainHome adapter = new AdapterMainHome(ctx, banner_list, product_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
