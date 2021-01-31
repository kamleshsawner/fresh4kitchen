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

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.ProductDetailActivity;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.Utility;


public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.MyViewHolder> {

    Context ctx;
    List<Products> list;

    public HomeProductsAdapter(Context ctx, List<Products> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_product_view, parent, false);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(ctx, ProductDetailActivity.class)
                        .putExtra("id", products.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_adpcate_image);
            name_tv = itemView.findViewById(R.id.tv_adpcate_name);
        }
    }
}
