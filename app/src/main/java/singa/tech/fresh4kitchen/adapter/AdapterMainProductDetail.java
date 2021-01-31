package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.components.AutoScrollViewPager;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.model.Varient;
import singa.tech.fresh4kitchen.util.Constant;
import singa.tech.fresh4kitchen.util.ProjectUtility;
import singa.tech.fresh4kitchen.util.SessionManager;


public class AdapterMainProductDetail extends RecyclerView.Adapter<AdapterMainProductDetail.MyHolder> {

    Context ctx;
    Products product;
    SessionManager sessionManager;

    public AdapterMainProductDetail(Context ctx, Products product) {
        this.ctx = ctx;
        this.product = product;
        sessionManager = new SessionManager(ctx);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = null;
        if (i == 0) {
            itemView = LayoutInflater.from(ctx).inflate(R.layout.adp_home_top, viewGroup, false);
        }
        if (i == 1) {
            itemView = LayoutInflater.from(ctx).inflate(R.layout.layout_home_recycler, viewGroup, false);
        }
        if (i == 2) {
            itemView = LayoutInflater.from(ctx).inflate(R.layout.adp_proddetail_bottom, viewGroup, false);
        }
        return new MyHolder(itemView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        if (i == 0) {
            ArrayList<String> banner_list = ProjectUtility.getBannerList(product);
            if (banner_list.size() > 0) {
                PagerAdapter pagerAdapter = new MyPagerAdapter(ctx, banner_list, myHolder.autoScrollViewPager);
                myHolder.autoScrollViewPager.setAdapter(pagerAdapter);
                myHolder.autoScrollViewPager.startAutoScroll();
                myHolder.autoScrollViewPager.setInterval(Constant.SLIDER_INTERVEL);
                myHolder.circleIndicator.setViewPager(myHolder.autoScrollViewPager);
            }
        } else if (i == 1) {
            myHolder.head_tv.setText("Pack Size");
            ArrayList<Varient> varient_list = ProjectUtility.getVarientList(product);
            myHolder.recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
            myHolder.recyclerView.setLayoutManager(layoutManager);
            DetailVarientAdapter adapter = new DetailVarientAdapter(ctx, varient_list);
            myHolder.recyclerView.setAdapter(adapter);
        } else if (i == 2) {
            myHolder.about_tv.setText(product.getAbout());
            myHolder.benefit_tv.setText(product.getBenifit());
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // top
        AutoScrollViewPager autoScrollViewPager;
        CircleIndicator circleIndicator;

        // mid
        RecyclerView recyclerView;
        TextView head_tv;

        // bottom
        TextView about_tv, benefit_tv;


        public MyHolder(@NonNull View itemView, int i) {
            super(itemView);
            if (i == 0) {
                autoScrollViewPager = itemView.findViewById(R.id.asvp_home_viewpager);
                circleIndicator = itemView.findViewById(R.id.ci_home_indicator);
            } else if (i == 1) {
                recyclerView = itemView.findViewById(R.id.rv_homeadp_recyclerview);
                head_tv = itemView.findViewById(R.id.tv_mid_heading);
            } else if (i == 2) {
                about_tv = itemView.findViewById(R.id.tv_proddetail_about);
                benefit_tv = itemView.findViewById(R.id.tv_proddetail_benefit);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
