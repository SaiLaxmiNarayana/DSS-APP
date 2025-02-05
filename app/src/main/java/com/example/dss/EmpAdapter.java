package com.example.dss;// DataAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.ViewHolder> {

    private List<EmpModel> dataList;
    private Context context;

    public EmpAdapter(List<EmpModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmpModel dataModel = dataList.get(position);

        // Set data to views in the row
        holder.projectTextView.setText("Name:" + dataModel.getProject());
        holder.locationTextView.setText("Location:" + dataModel.getPhysicalLocation());
        holder.stateTextView.setText("State:" + dataModel.getState());
        holder.hoursWorkedTextView.setText("Hours:" + dataModel.getNoOfHoursWorked());
        holder.calendarTextView.setText("Date:" + dataModel.getCalendar());
        holder.inTimeTextView.setText("In Time:" + dataModel.getInTime());
        holder.outTimeTextView.setText("Out Time" + dataModel.getOutTime());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView projectTextView;
        TextView locationTextView;
        TextView stateTextView;
        TextView hoursWorkedTextView;

        TextView calendarTextView;
        TextView inTimeTextView;
        TextView outTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectTextView = itemView.findViewById(R.id.projectText);
            locationTextView = itemView.findViewById(R.id.locationText);
            stateTextView = itemView.findViewById(R.id.stateText);
            hoursWorkedTextView = itemView.findViewById(R.id.hoursWorkedText);

            calendarTextView = itemView.findViewById(R.id.calendarText);
            inTimeTextView = itemView.findViewById(R.id.inTimeText);
            outTimeTextView = itemView.findViewById(R.id.outTimeText);
         // Add this line

        }
    }
}
