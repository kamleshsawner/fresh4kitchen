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
import singa.tech.fresh4kitchen.adapter.MyOrderAdapter;
import singa.tech.fresh4kitchen.model.MyOrder;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

@SuppressLint("ValidFragment")
public class MyOrderFragment extends Fragment {

    Context ctx;
    View view = null;

    RecyclerView recyclerView;
    ImageView nodata_iv;
    SessionManager sessionManager;

    @SuppressLint("ValidFragment")
    public MyOrderFragment(Context ctx) {
        this.ctx = ctx;
        sessionManager = new SessionManager(ctx);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_recycler, null);
        initXml();
        if (ConnectionDetector.isConnected()) {
            getMyOrders(ApiObject.myOrder(sessionManager.getData(SessionManager.KEY_ID)));
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
        return view;
    }

    private void initXml() {
        recyclerView = view.findViewById(R.id.rv_layout_recyclerview);
        nodata_iv = view.findViewById(R.id.iv_layout_nodata);

        recyclerView.setVisibility(View.GONE);
        nodata_iv.setVisibility(View.VISIBLE);
    }

    private void getMyOrders(JSONObject object) {

        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.MYORDER_API)
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
                JSONArray data_array = response.getJSONArray("data");
                ArrayList<MyOrder> product_list = JsonParser.getMyOrder(data_array);
                setAdapter(product_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<MyOrder> product_list) {

        if (product_list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
            MyOrderAdapter adapter = new MyOrderAdapter(ctx, product_list);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

}
