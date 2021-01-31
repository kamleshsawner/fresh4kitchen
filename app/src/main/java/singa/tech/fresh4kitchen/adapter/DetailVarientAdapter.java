package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.ProductDetailActivity;
import singa.tech.fresh4kitchen.model.Varient;


public class DetailVarientAdapter extends RecyclerView.Adapter<DetailVarientAdapter.MyViewHolder> {

    Context ctx;
    List<Varient> list;
    private int lastSelectedPosition = -1;

    public DetailVarientAdapter(Context ctx, List<Varient> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_varient, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Varient varient = list.get(position);
        holder.weight_tv.setText(varient.getWeight());
        holder.price_tv.setText("₹ " + varient.getActual_price());
        holder.off_price_tv.setText("₹ " + varient.getOffer_price());
        holder.price_tv.setPaintFlags(holder.price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.radioButton.setChecked(lastSelectedPosition == position);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.weight_tv.setText(list.get(position).getWeight());
                holder.off_price_tv.setText("₹ " + list.get(position).getOffer_price());
                holder.price_tv.setText("₹ " + list.get(position).getActual_price());
                holder.per_tv.setText(list.get(position).getPercentage() + " %");

                ((ProductDetailActivity) ctx).setValue(list.get(position).getActual_price(),
                        list.get(position).getVarient_id(),
                        list.get(position).getProduct_id());

                lastSelectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView weight_tv, price_tv, off_price_tv, per_tv;
        RadioButton radioButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            weight_tv = itemView.findViewById(R.id.tv_adpprodet_weight);
            price_tv = itemView.findViewById(R.id.tv_adpprodet_actulprice);
            off_price_tv = itemView.findViewById(R.id.tv_adpprodet_offerprice);
            per_tv = itemView.findViewById(R.id.tv_adpprodet_percentage);
            radioButton = itemView.findViewById(R.id.rb_adpprodet_button);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
