package singa.tech.fresh4kitchen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.CouponActivity;
import singa.tech.fresh4kitchen.model.Coupon;


public class AdapterCoupon extends RecyclerView.Adapter<AdapterCoupon.MyHolder> {

    Context ctx;
    List<Coupon> data_list;
    String total;

    public AdapterCoupon(Context ctx, List<Coupon> data_list) {
        this.ctx = ctx;
        this.data_list = data_list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.adapter_coupon, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        final Coupon coupon = data_list.get(i);
        myHolder.code_tv.setText(coupon.getCode());
        myHolder.discount_tv.setText(coupon.getDiscount() + " % OFF");
        myHolder.select_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("code", coupon.getCode());
                returnIntent.putExtra("dis", coupon.getDiscount());
                ((CouponActivity) ctx).setResult(Activity.RESULT_OK, returnIntent);
                ((CouponActivity) ctx).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView code_tv, discount_tv;
        Button select_bt;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            code_tv = itemView.findViewById(R.id.tv_adpcoupon_code);
            discount_tv = itemView.findViewById(R.id.tv_adpcoupon_discount);
            select_bt = itemView.findViewById(R.id.bt_adpcoupon_apply);
        }
    }
}
