package com.example.dss;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
import in.aabhasjindal.otptextview.OtpTextView ;


public class sendotp extends AppCompatActivity {


    private static final String OTP_URL = Constants.BASE_URL + "Overall.php";

    private EditText mobileNumber;
    private OtpTextView otpTextView;

    private ALoadingDialog aLodingDialog;

    private CountryCodePicker countryCodePicker;
    private Button sendOtpButton;
    private Button validateOtpButton;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

        mobileNumber = findViewById(R.id.mobileNumber);
        otpTextView = findViewById(R.id.otpTextView);
        sendOtpButton = findViewById(R.id.sendOtp);
        validateOtpButton = findViewById(R.id.validateOtp);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        aLodingDialog = new ALoadingDialog(this);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aLodingDialog.show();
                sendOtp(); // Rename the function to better represent its purpose
            }
        });

        validateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                aLodingDialog.show();
                validateOtp();
            }
        });
    }

    private void sendOtp() {


        String apiKey = ApiKeyManager.getInstance().getApiKey();
        final String mobile = mobileNumber.getText().toString().trim();
        final String enteredOtp = otpTextView.getOTP();
        final String countryCode = countryCodePicker.getSelectedCountryCode();

        String fullPhoneNumber = "+" + countryCode + mobile;

        if (mobile.isEmpty()) {
            aLodingDialog.cancel();
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest sendOtpRequest = new StringRequest(Request.Method.POST, OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", response);

                        try {
                            // Parse the response as JSON
                            JSONObject jsonObject = new JSONObject(response);

                            // Check if the response contains 'status' key
                            if (jsonObject.has("status")) {
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                Toast.makeText(sendotp.this, message, Toast.LENGTH_SHORT).show();

                                if ("success".equals(status)) {

                                    aLodingDialog.cancel();
                                    // OTP sent successfully
                                } else {
                                    // Handle other cases if needed
                                }
                            } else {
                                // Handle invalid JSON response from the server
                                Toast.makeText(sendotp.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            aLodingDialog.cancel();
                            Toast.makeText(sendotp.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        aLodingDialog.cancel();
                        Toast.makeText(sendotp.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("send_otp" ,"true") ;
             //   params.put("action", "send_otp");
                params.put("mobile", fullPhoneNumber);
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
        Volley.newRequestQueue(this).add(sendOtpRequest);
    }

    private void validateOtp() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        final String mobile = mobileNumber.getText().toString().trim();
        final String enteredOtp = otpTextView.getOTP();
        final String countryCode = countryCodePicker.getSelectedCountryCode();

        String fullPhoneNumber = "+" + countryCode + mobile;

        if (mobile.isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest validateOtpRequest = new StringRequest(Request.Method.POST, OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            Toast.makeText(sendotp.this, message, Toast.LENGTH_SHORT).show();

                            if ("success".equals(status)) {

                                aLodingDialog.cancel();
                                // Valid OTP, navigate to the next screen
                                Intent forgetPasswordIntent = new Intent(sendotp.this, Forgetpassword.class);
                                forgetPasswordIntent.putExtra("PhoneNo", fullPhoneNumber);
                                startActivity(forgetPasswordIntent);
                            } else {
                                // Handle invalid OTP
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(sendotp.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        aLodingDialog.cancel();
                        Toast.makeText(sendotp.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("validate_otp" ,"true") ;
             //   params.put("action", "validate_otp");
                params.put("mobile", fullPhoneNumber);
                params.put("entered_otp", enteredOtp);
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
        Volley.newRequestQueue(this).add(validateOtpRequest);
    }
}

