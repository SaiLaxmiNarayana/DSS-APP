package com.example.dss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Forgetpassword extends AppCompatActivity {

    private EditText newPassword, confirmPassword;
    private Button resetPasswordButton;
    private String phoneno;

    private static final String RESET_PASSWORD_URL = Constants.BASE_URL + "Overall.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Retrieve the desired username from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneno = extras.getString("PhoneNo");
        }

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        final String newPass = newPassword.getText().toString().trim();
        final String confirmPass = confirmPassword.getText().toString().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all details", Toast.LENGTH_LONG).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        // Pass the desired username to the PHP script
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server (success or error)
                        // You can parse the JSON response if needed
                        Toast.makeText(Forgetpassword.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        finish();
                        // Add logic for further actions if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Forgetpassword.this, "Error connecting to the server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reset_password","true") ;
                params.put("phoneno", phoneno);
                params.put("new_password", newPass);
                params.put("confirm_password", confirmPass);
                // Add any additional parameters needed for your PHP script
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
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
