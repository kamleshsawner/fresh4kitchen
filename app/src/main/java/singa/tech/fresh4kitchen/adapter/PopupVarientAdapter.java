package singa.tech.fresh4kitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.model.Varient;


public class PopupVarientAdapter extends RecyclerView.Adapter<PopupVarientAdapter.MyViewHolder> {

    Context ctx;
    List<Varient> list;
    Dialog dialog;
    CateProductsAdapter.MyViewHolder pre_holder;

    public PopupVarientAdapter(Context ctx, List<Varient> list, Dialog dialog, CateProductsAdapter.MyViewHolder pre_holder) {
        this.ctx = ctx;
        this.list = list;
        this.dialog = dialog;
        this.pre_holder = pre_holder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_adp_varient, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Varient varient = list.get(position);
        holder.weight_tv.setText(varient.getWeight());
        holder.price_tv.setText(varient.getActual_price());
        holder.off_price_tv.setText(varient.getOffer_price());
        holder.price_tv.setPaintFlags(holder.price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_holder.weight_tv.setText(list.get(position).getWeight());
                pre_holder.offprice_tv.setText("" + list.get(position).getOffer_price());
                pre_holder.actprice_tv.setText("₹ " + list.get(position).getActual_price());
                pre_holder.verient_tv.setText(list.get(position).getVarient_id());
                pre_holder.offer_tv.setText(list.get(position).getPercentage() + " %");
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView weight_tv, price_tv, off_price_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            weight_tv = itemView.findViewById(R.id.tv_adpvar_weight);
            price_tv = itemView.findViewById(R.id.tv_adpvar_actualprice);
            off_price_tv = itemView.findViewById(R.id.tv_adpvar_offerprice);
        }
    }
}
