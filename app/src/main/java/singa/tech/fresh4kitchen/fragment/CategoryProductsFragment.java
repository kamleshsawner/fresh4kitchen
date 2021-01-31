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
import android.widget.Button;
import android.widget.ProgressBar;
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
import singa.tech.fresh4kitchen.adapter.CateProductsAdapter;
import singa.tech.fresh4kitchen.adapter.PaginationListener;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.JsonParser;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;

@SuppressLint("ValidFragment")
public class CategoryProductsFragment extends Fragment implements View.OnClickListener {

    Context ctx;
    View view = null;

    Button fruits_bt, veg_bt, organic_bt;
    TextView selected_tv;
    RecyclerView recyclerView;
    String mode;
    ArrayList<Products> product_list;

    // pagination
    ProgressBar progressBar;
    int page = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;


    SessionManager sessionManager;

    @SuppressLint("ValidFragment")
    public CategoryProductsFragment(Context ctx, String mode) {
        this.ctx = ctx;
        this.mode = mode;
        sessionManager = new SessionManager(ctx);
        product_list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cate_product, null);
        initXml();
        getData();
        return view;
    }

    private void initXml() {
        fruits_bt = view.findViewById(R.id.bt_cateproducts_fruites);
        veg_bt = view.findViewById(R.id.bt_cateproducts_veg);
        organic_bt = view.findViewById(R.id.bt_cateproducts_organic);
        selected_tv = view.findViewById(R.id.tv_cateproducts_selected);
        recyclerView = view.findViewById(R.id.rv_cateprod_recycler);
        progressBar = view.findViewById(R.id.pb_layout_progress);

        fruits_bt.setOnClickListener(this);
        veg_bt.setOnClickListener(this);
        organic_bt.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                getnextData();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void setTitle() {
        if (mode.equals("0")) {
            selected_tv.setText("Fruits");
        } else if (mode.equals("1")) {
            selected_tv.setText("Vegetables");
        } else if (mode.equals("2")) {
            selected_tv.setText("Namkeen");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_cateproducts_fruites:
                mode = "0";
                getData();
                break;

            case R.id.bt_cateproducts_organic:
                mode = "2";
                getData();
                break;

            case R.id.bt_cateproducts_veg:
                mode = "1";
                getData();
                break;
        }
    }

    private void getData() {
        page = 0;
        product_list = new ArrayList<>();
        setTitle();
        if (ConnectionDetector.isConnected()) {
            getCateproducts(ApiObject.getCategoryProduct(mode, page + ""));
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
    }

    private void getnextData() {
        setTitle();
        if (ConnectionDetector.isConnected()) {
            getCateproducts(ApiObject.getCategoryProduct(mode, page + ""));
        } else {
            Utility.toastView(ctx, ctx.getString(R.string.no_internet));
        }
    }


    private void getCateproducts(JSONObject object) {

        if (page == 0) {
            Utility.showLoader(ctx);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        AndroidNetworking.post(Webapi.CATEGORY_API)
                .addJSONObjectBody(object)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (page == 0) {
                            Utility.hideLoader();
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                        setResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (page == 0) {
                            Utility.hideLoader();
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                        Utility.toastView(ctx, anError.toString());
                    }
                });
    }

    private void setResponse(JSONObject response) {
        page++;
        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                JSONArray data_array = response.getJSONArray("data");
                ArrayList<Products> list = JsonParser.getCateProductlist(data_array, product_list);
                setAdapter(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<Products> list) {

        String userid = sessionManager.getData(SessionManager.KEY_ID);

        isLoading = false;
        recyclerView.setHasFixedSize(true);
        CateProductsAdapter adapter = new CateProductsAdapter(ctx, list, userid);
        if (page != 1) {
            adapter.notifyDataSetChanged();
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

}
