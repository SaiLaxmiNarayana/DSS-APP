package com.example.dss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> implements Filterable {

    private ArrayList<details> dataList;
    private ArrayList<details> dataListFull; // Copy of original list for filtering

    // Constructor
    public DetailsAdapter(ArrayList<details> dataList) {
        this.dataList = dataList;
        dataListFull = new ArrayList<>(dataList); // Initialize the full list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        details data = dataList.get(position);
        holder.txtName.setText("" + data.getName());
        holder.txtDesignation.setText("" + data.getUserid());
        holder.txtDepartment.setText("" + data.getEmail());
        holder.txtDays.setText("" + data.getPhoneno());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesignation, txtDepartment, txtDays;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.username);
            txtDesignation = itemView.findViewById(R.id.id);
            txtDepartment = itemView.findViewById(R.id.email);
            txtDays = itemView.findViewById(R.id.phoneNumber);
        }
    }
    public void setFilter(ArrayList<details> filteredList) {
        dataList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<details> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (details item : dataListFull) {
                    if (item.getUserrole().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList.clear();
            dataList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
