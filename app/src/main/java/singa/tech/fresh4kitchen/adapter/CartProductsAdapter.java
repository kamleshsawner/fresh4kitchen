package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.ProductDetailActivity;
import singa.tech.fresh4kitchen.activity.CartActivity;
import singa.tech.fresh4kitchen.model.Cart;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;


public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<Cart> list;
    String userid;

    public CartProductsAdapter(Context ctx, ArrayList<Cart> list, String userid) {
        this.ctx = ctx;
        this.list = list;
        this.userid = userid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_cartproduct_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Cart products = list.get(position);
        holder.name_tv.setText(products.getName());
        if (!products.getCover_img().equals("")) {
            Utility.loadImage(ctx, products.getCover_img(), holder.imageView);
        }
        holder.offprice_tv.setText("" + products.getPrice());
        holder.weight_tv.setText(products.getUnit());
        holder.verient_tv.setText(products.getVerient_id());
        holder.qty_tv.setText(products.getQty());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(ctx, ProductDetailActivity.class)
                        .putExtra("id", list.get(position).getProduct_id()));
            }
        });
        holder.add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isConnected()) {
                    updatecart(true, holder.qty_tv, ApiObject.getUpdatecart
                            (Utility.getQty(true, holder.qty_tv.getText().toString()), userid, holder.verient_tv.getText().toString()), position);
                }
            }
        });
        holder.remove_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isConnected()) {
                    if (holder.qty_tv.getText().toString().equals("1")) {
                        removecart(position, ApiObject.getRemovecart(products.getUser_id(), products.getVerient_id()));
                    } else {
                        updatecart(false, holder.qty_tv, ApiObject.getUpdatecart
                                (Utility.getQty(false, holder.qty_tv.getText().toString()), userid, holder.verient_tv.getText().toString()), position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, add_iv, remove_iv;
        TextView name_tv, offer_tv, weight_tv, offprice_tv, actprice_tv, verient_tv, qty_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_adpcart_image);
            add_iv = itemView.findViewById(R.id.iv_adpcart_add);
            remove_iv = itemView.findViewById(R.id.iv_adpcart_remove);
            name_tv = itemView.findViewById(R.id.tv_adpcart_name);
            offer_tv = itemView.findViewById(R.id.tv_adpcart_offer);
            weight_tv = itemView.findViewById(R.id.tv_adpcart_weight);
            offprice_tv = itemView.findViewById(R.id.tv_adpcart_offerprice);
            actprice_tv = itemView.findViewById(R.id.tv_adpcart_actualprice);
            verient_tv = itemView.findViewById(R.id.tv_adpcart_verientid);
            qty_tv = itemView.findViewById(R.id.tv_adpcart_qty);
        }
    }

    // update cart
    private void updatecart(final boolean add, final TextView qty_tv, JSONObject object, final int position) {
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
                        setResponse(response, add, qty_tv, position);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setResponse(JSONObject response, boolean add, TextView qty_tv, int position) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                String qty = Utility.getQty(add, qty_tv.getText().toString());
                qty_tv.setText(qty);
                list.get(position).setQty(qty);
                ((CartActivity) ctx).setTotal(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // remove cart
    private void removecart(final int pos, JSONObject object) {
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
                        setDeleteResponse(response, pos);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setDeleteResponse(JSONObject response, int position) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                list.remove(position);
                notifyDataSetChanged();
                ((CartActivity) ctx).setTotal(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
