package com.example.dss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.LeaveRequestViewHolder> {

    private List<LeaveRequest> leaveRequestList;

    public LeaveRequestAdapter(List<LeaveRequest> leaveRequestList) {
        this.leaveRequestList = leaveRequestList;
    }

    @NonNull
    @Override
    public LeaveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_request, parent, false);
        return new LeaveRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestViewHolder holder, int position) {
        LeaveRequest leaveRequest = leaveRequestList.get(position);
        holder.bind(leaveRequest);
    }

    @Override
    public int getItemCount() {
        return leaveRequestList.size();
    }

    public class LeaveRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserId, textViewStartDate, textViewEndDate, textViewStatus;

        public LeaveRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserId = itemView.findViewById(R.id.textViewUserId);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }

        public void bind(LeaveRequest leaveRequest) {
            textViewUserId.setText("User ID: " + leaveRequest.getUserId());
            textViewStartDate.setText("Start Date: " + leaveRequest.getStartDate());
            textViewEndDate.setText("End Date: " + leaveRequest.getEndDate());
            textViewStatus.setText("Status: " + leaveRequest.getStatus());
        }
    }
}