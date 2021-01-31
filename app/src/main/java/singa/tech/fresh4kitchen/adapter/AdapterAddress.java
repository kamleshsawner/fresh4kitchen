package singa.tech.fresh4kitchen.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import singa.tech.fresh4kitchen.R;
import singa.tech.fresh4kitchen.activity.TimeslotActivity;
import singa.tech.fresh4kitchen.model.Address;
import singa.tech.fresh4kitchen.util.ApiObject;
import singa.tech.fresh4kitchen.util.ConnectionDetector;
import singa.tech.fresh4kitchen.util.Utility;
import singa.tech.fresh4kitchen.util.Webapi;


public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.MyHolder> {

    Context ctx;
    List<Address> data_list;
    String total;

    public AdapterAddress(Context ctx, List<Address> data_list, String total) {
        this.ctx = ctx;
        this.data_list = data_list;
        this.total = total;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.adapter_address, viewGroup, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        final Address address = data_list.get(i);
        myHolder.name_tv.setText(address.getName());
        myHolder.mobile_tv.setText(address.getMobile());
        final String add = address.getAddress() + "\n" + address.getCity() + "," + address.getState() + "," + address.getZip();
        myHolder.address_tv.setText(add);
        myHolder.select_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(ctx, TimeslotActivity.class)
                        .putExtra("id", address.getId())
                        .putExtra("total", total));
            }
        });
        myHolder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isConnected()) {
                    deleteAddress(ApiObject.deleteAddress(address.getId()), i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView name_tv, mobile_tv, address_tv;
        Button select_bt;
        ImageView delete_iv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.tv_adpaddress_name);
            mobile_tv = itemView.findViewById(R.id.tv_adpaddress_mob);
            address_tv = itemView.findViewById(R.id.tv_adpaddress_address);
            select_bt = itemView.findViewById(R.id.bt_adpaddress_select);
            delete_iv = itemView.findViewById(R.id.iv_adpaddress_delete);
        }
    }

    // delete address
    private void deleteAddress(JSONObject object, final int pos) {
        Utility.showLoader(ctx);
        AndroidNetworking.post(Webapi.ADDRESS_API)
                .addJSONObjectBody(object)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.hideLoader();
                        setResponse(response, pos);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utility.hideLoader();
                    }
                });
    }

    private void setResponse(JSONObject response, int pos) {

        try {
            JSONObject info_obj = response.getJSONObject("info");
            String status = info_obj.getString("status");
            String msg = info_obj.getString("message");
            if (status.equals("200")) {
                data_list.remove(pos);
                notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
