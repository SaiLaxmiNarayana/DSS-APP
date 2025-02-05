package com.example.dss;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dss.DetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class detailsActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RETRIEVE_URL = Constants.BASE_URL + "Overall.php?Allemployeedetails=true";

    private RecyclerView recyclerView;
    DetailsAdapter adapter;
    private ArrayList<details> leaveApplicationsList;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recyclerView = findViewById(R.id.recyclerView);
        leaveApplicationsList = new ArrayList<>();
        adapter = new DetailsAdapter(leaveApplicationsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = parent.getItemAtPosition(position).toString();
                filterDataByUserRole(selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        retrieveData();
    }

    private void filterDataByUserRole(String selectedRole) {
        ArrayList<details> filteredList = new ArrayList<>();
        for (details leaveApplication : leaveApplicationsList) {
            if (leaveApplication.getUserrole().equalsIgnoreCase(selectedRole)) {
                filteredList.add(leaveApplication);
            }
        }
        adapter.setFilter(filteredList);
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

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject item = dataArray.getJSONObject(i);

                                    details leaveApplication = new details();
                                    leaveApplication.setName(item.getString("Username"));
                                    leaveApplication.setPassword(item.getString("Password"));
                                    leaveApplication.setEmail(item.getString("Email"));
                                    leaveApplication.setUserid(item.getString("Userid"));
                                    leaveApplication.setPhoneno(item.getString("PhoneNo"));
                                    leaveApplication.setUserrole(item.getString("Userrole"));

                                    leaveApplicationsList.add(leaveApplication);
                                }

                                adapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(detailsActivity.this, "Error: " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(detailsActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add the API key to the headers
                headers.put("Authorization", "Bearer " + apiKey);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
