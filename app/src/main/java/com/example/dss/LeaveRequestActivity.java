package com.example.dss;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.android.volley.DefaultRetryPolicy;
import com.airbnb.lottie.LottieAnimationView;



public class LeaveRequestActivity extends Activity {

    private static final String TAG = LeaveRequestActivity.class.getSimpleName();
    private EditText editTextName;
    private EditText editTextFromDate;
    private EditText editTextToDate;
    private EditText Reason;
    private EditText days;
    private EditText userid;
    private Button btnSubmit;
    private ImageView fromDateButton;
    private ImageView toDateButton;
    private Calendar calendar;
    private int year, month, day;

    private ALoadingDialog aLodingDialog;

    private static final String SUBMIT_URL = Constants.BASE_URL + "Overall.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);

        editTextName = findViewById(R.id.name);
        editTextFromDate = findViewById(R.id.from_date);
        editTextToDate = findViewById(R.id.to_date);
        btnSubmit = findViewById(R.id.submitButton);
        Reason = findViewById(R.id.reason);
        days=findViewById(R.id.days);
        userid = findViewById(R.id.id) ;
        aLodingDialog = new ALoadingDialog(this);


        fromDateButton = findViewById(R.id.fromDateButton);
        toDateButton = findViewById(R.id.toDateButton);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        // In your activity or fragment



        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextFromDate);
            }
        });

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextToDate);
            }
        });

        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFromDate.performClick();
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextToDate.performClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                aLodingDialog.show();

                submitForm();
            }


        });
    }




    private void showDatePickerDialog(final EditText dateEditText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveRequestActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        dateEditText.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void submitForm() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        final String name = editTextName.getText().toString().trim();

        final String from_date = editTextFromDate.getText().toString().trim();
        final String to_date = editTextToDate.getText().toString().trim();
        final String dayss = days.getText().toString().trim();
        final String reason = Reason.getText().toString().trim();


        final String userId = userid.getText().toString().trim() ;

        if (name.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please fill in all details", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "Submitting leave request...", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            Toast.makeText(LeaveRequestActivity.this, message, Toast.LENGTH_SHORT).show();

                            if ("success".equals(status)) {
                                aLodingDialog.cancel();
                                // Handle success, if needed

                            } else {
                                // Handle error, if needed
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null) {
                            Log.e(TAG, "Error: " + error.getMessage());
                            Toast.makeText(LeaveRequestActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error: Unknown error");
                            Toast.makeText(LeaveRequestActivity.this, "Error: Unknown error", Toast.LENGTH_SHORT).show();
                        }

                        if (error != null) {
                            error.printStackTrace();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("submit_leave" ,"true") ;
                params.put("name", name);
                params.put("from_date", from_date);
                params.put("to_date", to_date);
                params.put("dayss",dayss);
                params.put("reason", reason);
                params.put("userId" ,userId) ;
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,  // No retry
                0,  // No timeout
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(stringRequest);
    }
}
