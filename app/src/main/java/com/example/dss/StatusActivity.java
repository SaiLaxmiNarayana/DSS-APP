package com.example.dss;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatusActivity extends AppCompatActivity {

    private static final String TAG = StatusActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private Statusadapter adapter;

    public static final String USERID_KEY = "userid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Retrieve leave applications from the server
        retrieveLeaveApplications();
    }

    private void retrieveLeaveApplications() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        // Replace this URL with the actual URL for your server endpoint
        String Userid = getUserIdFromSharedPreferences();
      //  Log.d("TAG" ,Userid);
        Log.d("StatusActivity", "Userid: " + Userid);
        String url= Constants.BASE_URL + "Overall.php?userid=" + Userid + "&EmployeeStatus=true";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON_RESPONSE", response.toString());
                        ArrayList<Statusmodel> leaveApplications = parseJsonResponse(response);
                        adapter = new Statusadapter(leaveApplications);
                        recyclerView.setAdapter(adapter);

                        // Check if there are leave applications to show
                        if (leaveApplications.isEmpty()) {
                            // No leave applications, hide the RecyclerView
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            // There are leave applications, show the RecyclerView
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
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





        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                80000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private ArrayList<Statusmodel> parseJsonResponse(JSONObject response) {
        ArrayList<Statusmodel> leaveApplications = new ArrayList<>();

        try {
            JSONArray dataArray = response.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject leaveJson = dataArray.getJSONObject(i);
                Statusmodel leaveApp = new Statusmodel();
                leaveApp.setReason(leaveJson.getString("reason"));
                leaveApp.setStatus(leaveJson.getInt("status"));
              //  long timestampLong = leaveJson.getLong("timestamp1"); // Assuming timestamp is stored as a long in milliseconds
             //   Timestamp timestamp = new Timestamp(timestampLong);
                leaveApp.setTimestamp(Timestamp.valueOf(leaveJson.getString("timestamp1")));
                leaveApp.setfromdate(leaveJson.getString("fromdate"));
                leaveApp.setTodate(leaveJson.getString("todate"));
                leaveApplications.add(leaveApp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return leaveApplications;
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(USERID_KEY, null);
    }
}
