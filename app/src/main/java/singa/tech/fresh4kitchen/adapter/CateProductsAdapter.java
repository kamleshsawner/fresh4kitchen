package singa.tech.fresh4kitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.MainActivity;
import singa.tech.fresh4kitchen.activity.ProductDetailActivity;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.model.Varient;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;


public class CateProductsAdapter extends RecyclerView.Adapter<CateProductsAdapter.MyViewHolder> {

    Context ctx;
    List<Products> list;
    String userid;

    SessionManager sessionManager;

    public CateProductsAdapter(Context ctx, List<Products> list, String userid) {
        this.ctx = ctx;
        this.list = list;
        this.userid = userid;
        sessionManager = new SessionManager(ctx);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_cateproduct_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Products products = list.get(position);
        holder.name_tv.setText(products.getName());
        if (!products.getCover_img().equals("")) {
            Utility.loadImage(ctx, products.getCover_img(), holder.imageView);
        }
        final String[] offerPrice = products.getOffer_price().split(",");
        final String[] actualPrice = products.getPrice().split(",");
        final String[] Weight = products.getUnit().split(",");
        final String[] Percentage = products.getPercentage().split(",");
        final String[] Varient_id = products.getVerient_id().split(",");

        holder.offprice_tv.setText("" + offerPrice[0]);
        holder.actprice_tv.setText("₹ " + actualPrice[0]);
        holder.offer_tv.setText(Percentage[0] + " %");
        holder.weight_tv.setText(Weight[0]);
        holder.verient_tv.setText(Varient_id[0]);
        holder.actprice_tv.setPaintFlags(holder.actprice_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(ctx, ProductDetailActivity.class)
                        .putExtra("id", list.get(position).getId()));
            }
        });
        holder.weight_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVerient(offerPrice, actualPrice, Weight, Percentage, Varient_id, holder);
            }
        });
        holder.add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isConnected()) {
                    addTocart(ApiObject.getAddcart(products.getId(), userid, holder.offprice_tv.getText().toString(), holder.verient_tv.getText().toString()));
                }
            }
        });
    }

    private void showVerient(String[] offerPrice, String[] actualPrice, String[] weight, String[] percentage, String[] varient_id, MyViewHolder holder) {

        ArrayList<Varient> list = new ArrayList<>();
        for (int i = 0; i < offerPrice.length; i++) {
            list.add(new Varient("", "", varient_id[i], offerPrice[i],
                    actualPrice[i], weight[i], percentage[i]));
        }
        final Dialog dialog = new Dialog(ctx);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.dialog_varient);
        RecyclerView recyclerView = dialog.findViewById(R.id.rv_dialog_verient);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        PopupVarientAdapter adapter = new PopupVarientAdapter(ctx, list, dialog, holder);
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_tv, offer_tv, weight_tv, offprice_tv, actprice_tv, verient_tv;
        LinearLayout weight_ll;
        Button add_bt;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_adpprodlist_image);
            name_tv = itemView.findViewById(R.id.tv_adpprodlist_name);
            offer_tv = itemView.findViewById(R.id.tv_adpprodlist_offer);
            weight_tv = itemView.findViewById(R.id.tv_adpprodlist_weight);
            offprice_tv = itemView.findViewById(R.id.tv_adpprodlist_offerprice);
            actprice_tv = itemView.findViewById(R.id.tv_adpprodlist_actualprice);
            verient_tv = itemView.findViewById(R.id.tv_adpprodlist_verientid);
            weight_ll = itemView.findViewById(R.id.ll_adpprodlist_weight);
            add_bt = itemView.findViewById(R.id.bt_adpprodlist_add);
        }
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
                String counter = response.getString("data");
                sessionManager.setData(SessionManager.KEY_COUNTER, counter);
                ((MainActivity) ctx).setCounter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
