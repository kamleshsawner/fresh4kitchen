package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.components.AutoScrollViewPager;
import singa.tech.fresh4kitchen.fragment.CategoryProductsFragment;
import singa.tech.fresh4kitchen.model.Products;
import singa.tech.fresh4kitchen.util.Constant;
import singa.tech.fresh4kitchen.util.SessionManager;
import singa.tech.fresh4kitchen.util.Utility;


public class AdapterMainHome extends RecyclerView.Adapter<AdapterMainHome.MyHolder> {

    Context ctx;
    List<String> banner_list;
    List<Products> products_list;
    SessionManager sessionManager;

    public AdapterMainHome(Context ctx, List<String> banner_list, ArrayList<Products> products_list) {
        this.ctx = ctx;
        this.banner_list = banner_list;
        this.products_list = products_list;
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
            itemView = LayoutInflater.from(ctx).inflate(R.layout.adp_home_bottom, viewGroup, false);
        }
        return new MyHolder(itemView, i);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        if (i == 0) {
            if (banner_list.size() > 0) {
                PagerAdapter pagerAdapter = new MyPagerAdapter(ctx, banner_list, myHolder.autoScrollViewPager);
                myHolder.autoScrollViewPager.setAdapter(pagerAdapter);
                myHolder.autoScrollViewPager.startAutoScroll();
                myHolder.autoScrollViewPager.setInterval(Constant.SLIDER_INTERVEL);
                myHolder.circleIndicator.setViewPager(myHolder.autoScrollViewPager);
            }
        } else if (i == 1) {
            myHolder.recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(ctx, 4);
            myHolder.recyclerView.setLayoutManager(layoutManager);
            HomeProductsAdapter adapter = new HomeProductsAdapter(ctx, products_list);
            myHolder.recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // top
        AutoScrollViewPager autoScrollViewPager;
        CircleIndicator circleIndicator;
        LinearLayout fruits_ll, veg_ll, organic_ll, category_ll;

        // mid
        RecyclerView recyclerView;

        public MyHolder(@NonNull View itemView, int i) {
            super(itemView);
            if (i == 0) {
                autoScrollViewPager = itemView.findViewById(R.id.asvp_home_viewpager);
                circleIndicator = itemView.findViewById(R.id.ci_home_indicator);
                fruits_ll = itemView.findViewById(R.id.ll_hometop_fruits);
                veg_ll = itemView.findViewById(R.id.ll_hometop_veg);
                organic_ll = itemView.findViewById(R.id.ll_hometop_organic);
                category_ll = itemView.findViewById(R.id.ll_hometop_category);

                category_ll.setVisibility(View.VISIBLE);
                fruits_ll.setOnClickListener(this);
                veg_ll.setOnClickListener(this);
                organic_ll.setOnClickListener(this);
            } else if (1 == 1) {
                recyclerView = itemView.findViewById(R.id.rv_homeadp_recyclerview);
            }
        }

        @Override
        public void onClick(View view) {

            Fragment fragment = null;
            switch (view.getId()) {

                case R.id.ll_hometop_fruits:
                    fragment = new CategoryProductsFragment(ctx, "0");
                    Utility.setMainFragment(fragment, ctx);
                    break;

                case R.id.ll_hometop_veg:
                    fragment = new CategoryProductsFragment(ctx, "1");
                    Utility.setMainFragment(fragment, ctx);
                    break;

                case R.id.ll_hometop_organic:
                    fragment = new CategoryProductsFragment(ctx, "2");
                    Utility.setMainFragment(fragment, ctx);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
