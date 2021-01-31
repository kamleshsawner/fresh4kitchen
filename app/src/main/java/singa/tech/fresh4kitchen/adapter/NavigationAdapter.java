package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.model.Navigation;

public class NavigationAdapter extends BaseAdapter {

    Context ctx;
    ArrayList<Navigation> nav_list;

    public NavigationAdapter(Context ctx, ArrayList<Navigation> nav_list) {
        this.ctx = ctx;
        this.nav_list = nav_list;
    }

    @Override
    public int getCount() {
        return nav_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adp_navigation, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_nav_image);
        TextView name_tv = (TextView) view.findViewById(R.id.tv_adpnav_name);

        imageView.setImageResource(nav_list.get(position).getImage());
        name_tv.setText(nav_list.get(position).getName());

        if (position == 10) {
            imageView.setVisibility(View.GONE);
        }
        return view;
    }
}