package com.example.dss;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dss.Statusmodel;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Statusadapter extends RecyclerView.Adapter<Statusadapter.ViewHolder> {
    private static final String TAG = Statusadapter.class.getSimpleName();
    private String sample  ;
    private final ArrayList<Statusmodel> dataList;

    public Statusadapter(ArrayList<Statusmodel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statusdisplay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Statusmodel data = dataList.get(position);
         sample = data.getfromdate() ;
         Log.d(TAG,sample) ;
        // Check the status and set messages accordingly
        if (data.getStatus() == 0) {
            // Status 1: Leave rejected
            holder.txtStatusMessage.setText("Leave rejected. Reason: " + data.getReason());
            holder.timestamp1.setText(""+ data.getTimestamp().toString());
            holder.fromdate.setText("" + data.getfromdate().toString());
            holder.todate.setText("" + data.getTodatedate().toString());
            holder.txtStatusMessage.setVisibility(View.VISIBLE);
        } else {
            // Status 0: Leave accepted
            holder.txtStatusMessage.setText("Leave accepted at --> ");
            holder.timestamp1.setText( "" + data.getTimestamp().toString());
            holder.fromdate.setText("" + data.getfromdate().toString());
            holder.todate.setText("" + data.getTodatedate().toString());
            holder.txtStatusMessage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtReason, txtStatusMessage,timestamp1 ,fromdate ,todate ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReason = itemView.findViewById(R.id.txtReason);
            txtStatusMessage = itemView.findViewById(R.id.txtStatusMessage);
            timestamp1 = itemView.findViewById(R.id.timestamp) ;
            fromdate = itemView.findViewById(R.id.fromdate);
            todate = itemView.findViewById(R.id.todate) ;

        }
    }
}
