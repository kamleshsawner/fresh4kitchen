package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.model.MyOrder;
import singa.tech.fresh4kitchen.util.Utility;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<MyOrder> list;

    public MyOrderAdapter(Context ctx, ArrayList<MyOrder> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_myorder_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        MyOrder products = list.get(position);
        holder.name_tv.setText(products.getName());
        holder.orderid_tv.setText("OrderId : #" + products.getOrder_id());
        if (!products.getImage().equals("")) {
            Utility.loadImage(ctx, products.getImage(), holder.imageView);
        }
        holder.date_tv.setText(products.getDate());
        holder.price_tv.setText("" + products.getPrice());
        holder.weight_tv.setText(products.getVarient());
        holder.qty_tv.setText("QTY : " + products.getQty());
        if (products.getStatus().equals("0")) {
            holder.status_tv.setText("Status : Pending");
        } else if (products.getStatus().equals("1")) {
            holder.status_tv.setText("Status : Order Accepted");
        } else if (products.getStatus().equals("2")) {
            holder.status_tv.setText("Status : Order Dispatched for delivery");
        } else if (products.getStatus().equals("3")) {
            holder.status_tv.setText("Status : Delivered");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView orderid_tv, name_tv, date_tv, weight_tv, status_tv, price_tv, qty_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_adpmyorder_image);
            orderid_tv = itemView.findViewById(R.id.tv_adpmyorder_orderid);
            name_tv = itemView.findViewById(R.id.tv_adpmyorder_name);
            date_tv = itemView.findViewById(R.id.tv_adpmyorder_date);
            weight_tv = itemView.findViewById(R.id.tv_adpmyorder_weight);
            status_tv = itemView.findViewById(R.id.tv_adpmyorder_status);
            price_tv = itemView.findViewById(R.id.tv_adpmyorder_price);
            qty_tv = itemView.findViewById(R.id.tv_adpmyorder_qty);
        }
    }
}
