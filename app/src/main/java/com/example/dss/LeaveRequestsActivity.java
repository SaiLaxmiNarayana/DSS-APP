package com.example.dss;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaveRequestsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RETRIEVE_URL = Constants.BASE_URL + "Overall.php?LeaveRequests=true";
    private RecyclerView recyclerView;
    private ArrayList<Leavemodel> originalLeaveApplicationsList;
    private SearchView searchView;
    private Empleaveadaptor adapter;
    private ArrayList<Leavemodel> leaveApplicationsList;

   // private Shimmereffect shimmer ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_requests);

        recyclerView = findViewById(R.id.recyclerView);
        leaveApplicationsList = new ArrayList<>();
        adapter = new Empleaveadaptor(leaveApplicationsList);
         // shimmer = new Shimmereffect(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        searchView = findViewById(R.id.searchView);
        setupSearchView();
       // shimmer.show();
        retrieveData();
    }
    private void retrieveData() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RETRIEVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if ("success".equals(status)) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                             //   shimmer.cancel();
                                ArrayList<Leavemodel> newDataList = new ArrayList<>();

                                // Process the retrieved data as needed
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject item = dataArray.getJSONObject(i);


                                        // Create a LeaveApplication object and add it to the list
                                        Leavemodel leaveApplication = new Leavemodel();
                                        leaveApplication.setName(item.getString("name"));
                                        leaveApplication.setDays(item.getString("days"));
                                        leaveApplication.setFromDate(item.getString("from_date"));
                                        leaveApplication.setToDate(item.getString("to_date"));
                                        leaveApplication.setReason(item.getString("reason"));
                                        leaveApplication.setUserId(item.getString("Userid"));

                                        leaveApplicationsList.add(leaveApplication);





                                }
                                originalLeaveApplicationsList = new ArrayList<>(leaveApplicationsList);

                                // Notify the adapter that the data has changed
                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);

                                // Show the RecyclerView
                            } else {
                                // Handle error, if needed
                                Toast.makeText(LeaveRequestsActivity.this, "Error: " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(LeaveRequestsActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add the API key to the headers
                headers.put("Authorization", "Bearer " + apiKey);
                Log.d(TAG, "API Key: " + apiKey);
                return headers;
            }


        };

        // Adding the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Handle the SearchView close event
                adapter.resetData(originalLeaveApplicationsList);
                return false; // Return false to allow default close behavior
            }
        });
    }
    private void filterData(String query) {
        ArrayList<Leavemodel> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // If the query is empty, reset to the original data
            adapter.resetData(originalLeaveApplicationsList);
        } else {
            // Otherwise, filter based on the query
            for (Leavemodel application : leaveApplicationsList) {
                if (application.getUserId().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(application);
                }
            }

            // Update the adapter with the filtered data
            adapter.resetData(filteredList);
            adapter.notifyDataSetChanged();
        }
    }



}
