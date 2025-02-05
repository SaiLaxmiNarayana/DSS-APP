package com.example.dss;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDailyWorkActivity extends AppCompatActivity {

    private EditText projectEditText;
    private EditText physicalLocationEditText;
    private EditText stateEditText;
    private EditText noOfHoursWorkedEditText;
    private EditText meetingIdEditText;
    private EditText typeEditText;
    private TextView dateTextView;
    private TextView inTimeTextView;
    private TextView outTimeTextView;

    private Spinner type ;
    private Button submitButton;
    private static final String TAG = EmployeeDailyWorkActivity.class.getSimpleName();
    private static final String SUBMIT_URL = Constants.BASE_URL + "Overall.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_daily_work);

        projectEditText = findViewById(R.id.project);
        physicalLocationEditText = findViewById(R.id.physicallocation);
        stateEditText = findViewById(R.id.state);
        noOfHoursWorkedEditText = findViewById(R.id.noofhoursworked);
        meetingIdEditText = findViewById(R.id.meetingid);
        type = findViewById(R.id.userRoleSpinner);

        dateTextView = findViewById(R.id.date);
        inTimeTextView = findViewById(R.id.intime);
        outTimeTextView = findViewById(R.id.out_time);
        submitButton = findViewById(R.id.submit);
        String userRole = UserManager.getUserRole();
        Log.d(TAG, "User ID retrieved: " + userRole);
        if (userRole != null) {
            // Now you have the user ID, and you can use it as needed in StoreActivity
        }
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        inTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(inTimeTextView);
            }
        });

        outTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(outTimeTextView);
            }
        });

        // Initially show today's date in dateTextView
        updateDateTextView();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.type_array,
                android.R.layout .simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);


    }

    private void submitForm() {
        String apiKey = ApiKeyManager.getInstance().getApiKey();
        final String Project= projectEditText.getText().toString().trim();
        final String PhysicalLocation= physicalLocationEditText.getText().toString().trim();
        final String State= stateEditText.getText().toString().trim();
        final String  NoOfHoursWorked= noOfHoursWorkedEditText.getText().toString().trim();
        final String MeetingId = meetingIdEditText.getText().toString().trim();
        final String Type = type.getSelectedItem().toString().trim() ;
        final String Calendar =dateTextView.getText().toString().trim();
        final String  InTime = inTimeTextView.getText().toString().trim();
        final String  OutTime = outTimeTextView.getText().toString().trim();
        if (Project.isEmpty() || PhysicalLocation.isEmpty() || State.isEmpty() || NoOfHoursWorked.isEmpty() || Type.isEmpty() || Calendar.isEmpty()|| InTime.isEmpty() || OutTime.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all details", Toast.LENGTH_LONG).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");



                                Toast.makeText(EmployeeDailyWorkActivity.this, message, Toast.LENGTH_SHORT).show();

                                if ("success".equals(status)) {
                                    // Handle success, if needed
                                } else {
                                    // Handle error, if needed
                                }
                            } else {
                                Log.e(TAG, "Invalid JSON format in response: " + response);
                                Toast.makeText(EmployeeDailyWorkActivity.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDailyWorkActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(EmployeeDailyWorkActivity.this, "Error submitting data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("DailyActivity_Submitting" ,"true") ;
                params.put("Project", Project);
                params.put(" PhysicalLocation", PhysicalLocation);
                params.put("State", State);
                params.put("NoOfHoursWorked", NoOfHoursWorked);
                params.put("MeetingId", MeetingId);
                params.put("Type", Type);
                params.put("Calendar", Calendar);
                params.put("InTime", InTime);
                params.put("OutTime", OutTime);
                params.put("Userrole", UserManager.getUserRole());
                return params;
            }

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

    private void showDatePicker() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Check if the selected date is today
                        if (year == currentYear && monthOfYear == currentMonth && dayOfMonth == currentDay) {
                            // Update TextView with selected date
                            // Format the selected date as "yyyy-MM-dd"
                            String selectedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            dateTextView.setText(selectedDate);

                            dateTextView.setText(selectedDate);
                        } else {
                            // Show a message or handle the case when the user selects a date other than today
                            // You may choose to show a Toast or some other UI element
                            // For now, I'll show a Toast message as an example
                            Toast.makeText(EmployeeDailyWorkActivity.this, "Please select today's date only", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                currentYear, currentMonth, currentDay // Set initial date based on current date
        );

        // Set minimum date allowed to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showTimePicker(final TextView timeTextView) {
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update TextView with selected time
                        String selectedTime = hourOfDay + ":" + minute;
                        timeTextView.setText(selectedTime);
                    }
                },
                hour, minute, DateFormat.is24HourFormat(this)
        );

        timePickerDialog.show();
    }

    private void updateDateTextView() {
        // Update dateTextView with today's date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        String todayDate = currentDay + "/" + (currentMonth + 1) + "/" + currentYear;
        dateTextView.setText(todayDate);
    }
}
