package com.example.dss ;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Empleaveadaptor extends RecyclerView.Adapter<Empleaveadaptor.ViewHolder> {
    private static final String TAG = Empleaveadaptor.class.getSimpleName();
    private final ArrayList<Leavemodel> dataList;
    public Empleaveadaptor(ArrayList<Leavemodel> dataList) {
        this.dataList = dataList;
    }

    public void resetData(ArrayList<Leavemodel> originalDataList) {
        dataList.clear();
        dataList.addAll(originalDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Leavemodel data = dataList.get(position);
        holder.txtName.setText("" + data.getName());
        holder.txtDays.setText("" + data.getDays());
        holder.txtFromDate.setText("" + data.getFromDate());
        holder.txtToDate.setText("" + data.getToDate());
        holder.txtReason.setText("" + data.getReason());
        holder.useridd.setText("" + data.getUserId());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDays, txtFromDate, txtToDate, txtReason, useridd;
        Button rejectButton, acceptButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDays = itemView.findViewById(R.id.txtDays);
            txtFromDate = itemView.findViewById(R.id.txtFromDate);
            txtToDate = itemView.findViewById(R.id.txtToDate);
            txtReason = itemView.findViewById(R.id.txtReason);
            useridd = itemView.findViewById(R.id.UserId);
            acceptButton = itemView.findViewById((R.id.myButton1));
            rejectButton = itemView.findViewById(R.id.myButton2);

            rejectButton.setOnClickListener(view -> showReasonDialog("Reject Reason"));
            acceptButton.setOnClickListener(view -> acceptLeave());
        }

        public void acceptLeave() {

            String apiKey = ApiKeyManager.getInstance().getApiKey();
            int status = 1; // Change this to the appropriate status for accepting
            String acceptReason = "Accepted";
            String userid = useridd.getText().toString().trim();
            String fromdate=txtFromDate.getText().toString().trim();
            String todate=txtToDate.getText().toString().trim();
            String name=txtName.getText().toString().trim();
            sendAcceptanceToServer(acceptReason, status, userid,fromdate,todate,name);

            // Remove the item from the RecyclerView
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                dataList.remove(position);
                notifyItemRemoved(position);
            }
        }

        private void sendAcceptanceToServer(String acceptReason, int status, String userid,String fromdate,String todate,String name ) {

            String apiKey = ApiKeyManager.getInstance().getApiKey();
            String url = Constants.BASE_URL + "Overall.php"; // Replace with your actual server URL
            Log.d(TAG,name);
            Log.d(TAG,fromdate);
            Log.d(TAG,todate);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response from the server (if needed)
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error from the server (if needed)
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("submit_reason", "true");
                    params.put("reason", acceptReason);
                    params.put("status", String.valueOf(status));
                    params.put("userid", userid);
                    params.put("fromdate",fromdate);
                    params.put("todate",todate);
                    params.put("name",name);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Add the API key to the headers
                    headers.put("Authorization", "Bearer " + apiKey);
                    return headers;
                }


            };

            // Adding the request to the queue
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }

        public void showReasonDialog(String dialogTitle) {
            final Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.dialog_reason);  // Use the correct layout for the dialog
            dialog.setTitle(dialogTitle);

            EditText editRejectReason = dialog.findViewById(R.id.editRejectReason);
            Button btnSubmitReject = dialog.findViewById(R.id.btnSubmitReject);

            btnSubmitReject.setOnClickListener(v -> {
                String rejectReason = editRejectReason.getText().toString().trim();
                String userid = useridd.getText().toString().trim();
                String fromdate=txtFromDate.getText().toString().trim();
                String todate=txtToDate.getText().toString().trim();
                String name=txtName.getText().toString().trim();

                // You can now handle the reject reason, for example, send it to the server
                int status = 0;
                submitReasonToServer(rejectReason, status, userid,fromdate,todate,name);
                dialog.dismiss();  // Dismiss the dialog after processing the reject reason
            });

            dialog.show();
        }

        private void submitReasonToServer(String rejectReason, int status, String userid,String fromdate,String todate,String name) {

            String apiKey = ApiKeyManager.getInstance().getApiKey();
            Log.d(TAG, "Submitting rejection reason to server...");

            String url =  Constants.BASE_URL + "Overall.php"; // Replace with your actual server URL

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle the response from the server (if needed)
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error from the server (if needed)
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("submit_reason", "true");
                    params.put("reason", rejectReason);
                    params.put("status", String.valueOf(status));
                    params.put("userid", userid);
                    params.put("fromdate",fromdate);
                    params.put("todate",todate);
                    params.put("name",name);
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Add the API key to the headers
                    headers.put("Authorization", "Bearer " + apiKey);
                    return headers;
                }


            };

            // Adding the request to the queue
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);

            // Remove the item from the RecyclerView
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                dataList.remove(position);
                notifyItemRemoved(position);
            }

            Log.d(TAG, "Rejection request added to the queue.");
        }
    }
}
