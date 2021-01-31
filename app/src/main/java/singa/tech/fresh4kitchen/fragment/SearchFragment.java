package singa.tech.fresh4kitchen.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import singa.tech.fresh4kitchen.adapter.CateProductsAdapter;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

@SuppressLint("ValidFragment")
public class SearchFragment extends Fragment {

    Context ctx;
    View view = null;

    RecyclerView recyclerView;
    ImageView search_iv;
    EditText text_et;
    ArrayList<Products> product_list;

    SessionManager sessionManager;

    @SuppressLint("ValidFragment")
    public SearchFragment(Context ctx) {
        this.ctx = ctx;
        sessionManager = new SessionManager(ctx);
        product_list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, null);
        initXml();
        return view;
    }

    private void initXml() {
        recyclerView = view.findViewById(R.id.rv_search_recyclerview);
        search_iv = view.findViewById(R.id.iv_search_search);
        text_et = view.findViewById(R.id.et_search_text);

        text_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ConnectionDetector.isConnected()) {
                    if (!text_et.getText().toString().equals("")) {
                        getSearching(ApiObject.getSearching(text_et.getText().toString()));
                    }
                } else {
                    Utility.toastView(ctx, ctx.getString(R.string.no_internet));
                }
            }
        });
    }

    private void getSearching(JSONObject object) {

        product_list = new ArrayList<>();
        AndroidNetworking.post(Webapi.SEARCH_API)
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
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
                product_list = JsonParser.getCateProductlist(data_array, product_list);
                setAdapter(product_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<Products> product_list) {

        String userid = sessionManager.getData(SessionManager.KEY_ID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        CateProductsAdapter adapter = new CateProductsAdapter(ctx, product_list, userid);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
