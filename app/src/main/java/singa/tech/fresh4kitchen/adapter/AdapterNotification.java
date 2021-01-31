package singa.tech.fresh4kitchen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.model.Notification;


public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyHolder> {

    Context ctx;
    List<Notification> data_list;

    public AdapterNotification(Context ctx, List<Notification> data_list) {
        this.ctx = ctx;
        this.data_list = data_list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.adapter_notification, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        Notification notification = data_list.get(i);
        myHolder.message_tv.setText(notification.getMessage());
        myHolder.date_tv.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView message_tv, date_tv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            message_tv = itemView.findViewById(R.id.tv_adpnoti_msg);
            date_tv = itemView.findViewById(R.id.tv_adpnoti_date);
        }
    }
}
