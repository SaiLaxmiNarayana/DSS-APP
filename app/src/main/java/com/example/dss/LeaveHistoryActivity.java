package com.example.dss;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveHistoryActivity extends AppCompatActivity {

    private SearchView editTextUserId;
    private TextView textViewLeaveCount;
    private Button buttonSearch;
    private RecyclerView recyclerViewLeaveRequests;
    private LeaveRequestAdapter adapter;
    private List<LeaveRequest> leaveRequestList;
    private RequestQueue requestQueue;
    private MaterialButton buttonStartDate;
    private MaterialButton buttonEndDate;
    private MaterialDatePicker<Long> startDatePicker;
    private MaterialDatePicker<Long> endDatePicker;
    private Spinner spinnerStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);

        // Initialize views
        editTextUserId = findViewById(R.id.editTextUserId);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerViewLeaveRequests = findViewById(R.id.recyclerViewLeaveRequests);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        textViewLeaveCount=findViewById(R.id.textViewLeaveCount);
        //spinnerStatus.setPrompt("Select Status"); // Set prompt for status spinner

        // Initialize RecyclerView
        leaveRequestList = new ArrayList<>();
        adapter = new LeaveRequestAdapter(leaveRequestList);
        recyclerViewLeaveRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLeaveRequests.setAdapter(adapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize MaterialButtons for selecting dates
        buttonStartDate = findViewById(R.id.buttonStartDate);
        buttonEndDate = findViewById(R.id.buttonEndDate);


        // Initialize MaterialDatePicker for start date
        startDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Start Date")
                .build();

        // Handle start date selection
        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            buttonStartDate.setText(getFormattedDate(selection));
        });

        // Show start date picker when start date MaterialButton is clicked
        buttonStartDate.setOnClickListener(v -> startDatePicker.show(getSupportFragmentManager(), "START_DATE_PICKER"));

        // Initialize MaterialDatePicker for end date
        endDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select End Date")
                .build();

        // Handle end date selection
        endDatePicker.addOnPositiveButtonClickListener(selection -> {
            buttonEndDate.setText(getFormattedDate(selection));
        });

        // Show end date picker when end date MaterialButton is clicked
        buttonEndDate.setOnClickListener(v -> endDatePicker.show(getSupportFragmentManager(), "END_DATE_PICKER"));

        // Button click listener for search
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLeaveHistory();
            }
        });
    }

    // Method to search leave history based on user input
    private void searchLeaveHistory() {


        String apiKey = ApiKeyManager.getInstance().getApiKey();
        // Get user input
        String userId = editTextUserId.getQuery().toString().trim();
        String startDate = buttonStartDate.getText().toString().trim();
        String endDate = buttonEndDate.getText().toString().trim();
        String selectedStatus = spinnerStatus.getSelectedItem().toString();
        Log.d(TAG, "selectedStatus: " + selectedStatus);

        if (selectedStatus.equals("SELECT STATUS")) {
            selectedStatus = "";
        }

        Log.d(TAG, "selectedStatus: " + selectedStatus);
        Log.d(TAG, "userId: " + userId);
        Log.d(TAG, "sdate: " + startDate);
        Log.d(TAG, "edate: " + endDate);
        // Check if start date and end date are not empty strings
        if (startDate.equals("Select Start Date")) {
            startDate = "";
        }
        if (endDate.equals("Select End Date")) {
            endDate = "";
        }

        // Perform validation if needed

        // Construct URL with parameters
        // Construct URL with parameters
        String url = String.format( Constants.BASE_URL + "Overall.php?Userid=%s&fromdate=%s&todate=%s&status=%s&leaveshistory=true", userId, startDate, endDate, selectedStatus);

// Make GET request using Volley
        String finalSelectedStatus = selectedStatus;
        //Log.d(TAG,finalSelectedStatus);
        // Make GET request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            leaveRequestList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (!userId.isEmpty() && jsonObject.getInt("Userid") != Integer.parseInt(userId)) {

                                    continue;
                                }
                                String statuss;
                                int userId = jsonObject.getInt("Userid");
                                String startDate = jsonObject.getString("fromdate");
                                String endDate = jsonObject.getString("todate");
                                Integer status = jsonObject.getInt("status");
                                Log.d(TAG, String.valueOf(status));

                                if(status==0){
                                    statuss="Rejected";
                                }
                                else{
                                    statuss="Approved";
                                }
//                                Log.d(TAG,statuss);

                                if (!finalSelectedStatus.isEmpty() && !statuss.equals(finalSelectedStatus)) {
                                    // Skip this leave request if status doesn't match
                                    continue;
                                }
                                // Create LeaveRequest object and add to list
                                LeaveRequest leaveRequest = new LeaveRequest(userId, startDate, endDate, statuss);
                                leaveRequestList.add(leaveRequest);
                                textViewLeaveCount.setText("Leave Count: " + leaveRequestList.size());

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                error.printStackTrace();
                Toast.makeText(LeaveHistoryActivity.this, "Error fetching leave history", Toast.LENGTH_SHORT).show();
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
        } ;

        // Add request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    // Method to convert timestamp to formatted date string
    private String getFormattedDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}