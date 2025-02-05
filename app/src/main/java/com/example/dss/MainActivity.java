package com.example.dss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText username, password, email, userid, phoneNo;
    private Spinner userrole;
    private Button btnSubmit;
    private TextView loginn;
    private Map<EditText, TextView> errorTextViews;

    private ALoadingDialog aLodingDialog;

    private CountryCodePicker countryCodePicker;

    private static final String SUBMIT_URL = Constants.BASE_URL + "Overall.php";

    String url = Constants.BASE_URL + "Overall.php";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        userid = findViewById(R.id.user_id);
        phoneNo = findViewById(R.id.Phonenumber);
        userrole = findViewById(R.id.userRoleSpinner);
        btnSubmit = findViewById(R.id.registerButton);
        loginn = findViewById(R.id.login);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        aLodingDialog = new ALoadingDialog(this);

        errorTextViews = new HashMap<>();

        btnSubmit.setEnabled(false);  // Disable the button initially

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                aLodingDialog.show();
                submitForm();
            }
        });

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(Login);
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.user_roles_array, // Create an array resource in your strings.xml file
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userrole.setAdapter(adapter);

        userrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedUserRole = userrole.getSelectedItem().toString();
                String userId = userid.getText().toString().trim();

                if (userId.matches("24dsin\\d{2}") && !selectedUserRole.equals("INTERN")) {
                    Toast.makeText(MainActivity.this, "Please select INTERN as User Role", Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(false);  // Disable the button
                } else if (userId.matches("24dsem\\d{2}") && !selectedUserRole.equals("EMPLOYEE")) {
                    Toast.makeText(MainActivity.this, "Please select EMPLOYEE as User Role", Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(false);  // Disable the button
                } else if (userId.matches("24dsad\\d{2}") && !selectedUserRole.equals("CEO")) {
                    Toast.makeText(MainActivity.this, "Please select ADMIN as User Role", Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(false);  // Disable the button
                } else {
                    btnSubmit.setEnabled(true);  // Enable the button
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void submitForm() {
        final String Username = username.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Userid = userid.getText().toString().trim();
        final String PhoneNo = phoneNo.getText().toString().trim();
        final String Userrole = userrole.getSelectedItem().toString().trim();
        final String countryCode = countryCodePicker.getSelectedCountryCode();

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        Log.d(TAG, "API Key: " + apiKey);

        String fullPhoneNumber = "+" + countryCode + PhoneNo;

        if (PhoneNo.isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            aLodingDialog.cancel();
            return;
        }

        if (Username.isEmpty() || PhoneNo.isEmpty() || Password.isEmpty() || Email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all details", Toast.LENGTH_LONG).show();
            aLodingDialog.cancel();
            return;
        }
        clearErrorTextViews();

        // Password validation
        if (!isValidPassword(Password)) {
            showError(username, "Password must contain at least one uppercase letter, one lowercase letter, and one special symbol");
            aLodingDialog.cancel();
            return;
        }

        // Phone number validation


        // Email validation
        if (!isValidEmail(Email)) {
            showError(email, "Invalid email address");
            aLodingDialog.cancel();
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


                                Toast.makeText(MainActivity.this, "Registered successfully! Now you can log in.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();

                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                                if ("success".equals(status)) {

                                   aLodingDialog.cancel();
                                    // Handle success, if needed
                                } else {
                                    // Handle error, if needed
                                }
                            } else {
                                Log.e(TAG, "Invalid JSON format in response: " + response);
                                Toast.makeText(MainActivity.this, "Enter UniQue Number", Toast.LENGTH_SHORT).show();
                                aLodingDialog.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                            aLodingDialog.cancel();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "Error submitting data", Toast.LENGTH_SHORT).show();
                        aLodingDialog.cancel();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("signup","true") ;
                params.put("Username", Username);
                params.put("Password", Password);
                params.put("Email", Email);
                params.put("Userid", Userid);
                params.put("PhoneNo", fullPhoneNumber);
                params.put("Userrole", Userrole);
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

        // Disable the button after submission

    }
    private void showError(EditText editText, String errorMessage) {
        TextView errorTextView = new TextView(this);
        errorTextView.setText(errorMessage);
        errorTextView.setTextColor(Color.RED);
        errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 0); // Adjust margins as needed
        errorTextView.setLayoutParams(layoutParams);

        // Add error TextView to the layout
        ((LinearLayout) editText.getParent()).addView(errorTextView);

        // Store EditText and corresponding error TextView in the map
        errorTextViews.put(editText, errorTextView);
    }

    // Method to clear existing error TextViews
    private void clearErrorTextViews() {
        for (TextView errorTextView : errorTextViews.values()) {
            ((LinearLayout) errorTextView.getParent()).removeView(errorTextView);
        }
        errorTextViews.clear();
    }
    private boolean isValidPassword(String password) {
        // At least one uppercase letter, one lowercase letter, and one special symbol
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    // Phone number validation method


    // Email validation method
    private boolean isValidEmail(String email) {
        // A simple email validation pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }



}
