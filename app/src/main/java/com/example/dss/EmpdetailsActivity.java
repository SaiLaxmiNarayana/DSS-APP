package com.example.dss;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpdetailsActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String PHP_SCRIPT_URL = Constants.BASE_URL + "Overall.php?Employeeattendence=true";
    private RecyclerView recyclerView;
    private EmpAdapter dataAdapter;
    private List<EmpModel> dataList;
    private Spinner monthSpinner;
    private Spinner userRoleSpinner;
    private EditText yearEditText; // EditText for year input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empdetails);

        // Reference to the RecyclerView in the layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list
        dataList = new ArrayList<>();

        // Initialize the RecyclerView adapter
        dataAdapter = new EmpAdapter(dataList, this);
        recyclerView.setAdapter(dataAdapter);

        // Initialize month spinner
        monthSpinner = findViewById(R.id.monthSpinner);

        // Set up array adapter for months
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Initialize user role spinner
        userRoleSpinner = findViewById(R.id.userRoleSpinner);
        ArrayAdapter<CharSequence> userRoleAdapter = ArrayAdapter.createFromResource(
                this, R.array.user_roles, android.R.layout.simple_spinner_item);
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(userRoleAdapter);

        // Initialize year EditText
        yearEditText = findViewById(R.id.yearEditText);

        // Call the method to fetch data using Volley
        fetchDataWithVolley();

        // Set up listeners for user interactions
        setupListeners();
    }

    private void setupListeners() {
        // Add a listener to fetch data when the user makes a selection for month
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fetchDataWithVolley();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                fetchDataWithVolley();
            }
        });

        // Add a listener to fetch data when the user makes a selection for user role
        userRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fetchDataWithVolley();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                fetchDataWithVolley();
            }
        });

        // Add a listener to fetch data when the user manually enters a year
        yearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                fetchDataWithVolley();
            }
        });
    }

    private void fetchDataWithVolley() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        int selectedMonth = monthSpinner.getSelectedItemPosition() + 1; // Adjust for 0-based index
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearEditText.getText().toString()); // Get year from EditText
        } catch (NumberFormatException e) {
            selectedYear = Calendar.getInstance().get(Calendar.YEAR); // Set default year if parsing fails
        }
        String selectedUserRole = userRoleSpinner.getSelectedItem().toString();

        String url = PHP_SCRIPT_URL + "?month=" + selectedMonth + "&year=" + selectedYear + "&userRole=" + selectedUserRole;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON data and populate the dataList
                        dataList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String project = jsonObject.getString("Project");
                                String physicalLocation = jsonObject.getString("PhysicalLocation");
                                String state = jsonObject.getString("State");
                                String noOfHoursWorked = jsonObject.getString("NoOfHoursWorked");
                                String meetingId = jsonObject.getString("MeetingId");
                                String type = jsonObject.getString("Type");
                                String calendar = jsonObject.getString("Calendar");
                                String inTime = jsonObject.getString("InTime");
                                String outTime = jsonObject.getString("OutTime");
                                String userRole=jsonObject.getString("Userrole");

                                // Create a DataModel object with extracted fields
                                EmpModel dataModel = new EmpModel(project, physicalLocation, state,
                                        noOfHoursWorked, meetingId, type, calendar, inTime, outTime,userRole);

                                // Add the DataModel object to the dataList
                                dataList.add(dataModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Notify the adapter that the data set has changed
                        dataAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching data", error);
                        // Handle error
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add the API key to the headers
                headers.put("Authorization", "Bearer " + apiKey);
                return headers;
            }

        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}