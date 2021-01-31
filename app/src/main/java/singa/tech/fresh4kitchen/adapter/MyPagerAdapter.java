package singa.tech.fresh4kitchen.adapter;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.components.AutoScrollViewPager;
import singa.tech.fresh4kitchen.util.Utility;


public class MyPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context ctx;
    List<String> layouts;
    AutoScrollViewPager viewPager;

    public MyPagerAdapter(Context ctx, List<String> layouts, AutoScrollViewPager viewPager) {
        this.ctx = ctx;
        this.layouts = layouts;
        this.viewPager = viewPager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_pager, container, false);
        ImageView bg_iv = view.findViewById(R.id.iv_intro_bg);
        String url = layouts.get(position);
        if (!url.equalsIgnoreCase("")) {
            Utility.loadImage(ctx, url, bg_iv);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

}
