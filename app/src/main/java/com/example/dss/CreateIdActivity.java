package com.example.dss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateIdActivity extends AppCompatActivity {
    Button add, delete,details; // Declare the delete button
    private static final String TAG = CreateIdActivity.class.getSimpleName();
    private static final String BASE_URL = Constants.BASE_URL;
    private static final String INSERT_URL = BASE_URL + "Overall.php";
    private static final String DELETE_URL = BASE_URL + "Overall.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_id);

        // Initialize the add and delete buttons
        add = findViewById(R.id.addButton);
        delete = findViewById(R.id.deletebutton); // Initialize the delete button
         details=findViewById(R.id.list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Add"); // Pass "Add" as the type of dialog
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Delete"); // Pass "Delete" as the type of dialog
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateIdActivity.this,detailsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showCustomDialog(String dialogType) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        EditText userIdEditText = dialogView.findViewById(R.id.dialog);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        EditText usernameEditText=dialogView.findViewById(R.id.usernamee);

        if (dialogType.equals("Add")) {
            dialogBuilder.setTitle("Add User");
        } else if (dialogType.equals("Delete")) {
            dialogBuilder.setTitle("Delete User");
        }

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();

                if (userId.isEmpty() || username.isEmpty()) {
                    Toast.makeText(CreateIdActivity.this, "Both fields are required", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed if any field is empty
                }

                if (dialogType.equals("Add")) {
                    // Call a method to add the user
                    addUser(INSERT_URL, userId, username);
                } else if (dialogType.equals("Delete")) {
                    // Call a method to delete the user
                    deleteUser(DELETE_URL, userId);
                }
                alertDialog.dismiss();
            }
        });
    }

    private void deleteUser(String url, String userId) {

        String apiKey = ApiKeyManager.getInstance().getApiKey();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                Toast.makeText(CreateIdActivity.this, message, Toast.LENGTH_SHORT).show();

                                if ("success".equals(status)) {
                                    // Handle success, if needed
                                } else {
                                    // Handle error, if needed
                                }
                            } else {
                                // Not a JSON object, handle as an error
                                Log.e(TAG, "Invalid JSON format in response: " + response);
                                Toast.makeText(CreateIdActivity.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateIdActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(CreateIdActivity.this, "Error submitting data", Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Delete" , "true");
                params.put("Userid", userId);
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
        Volley.newRequestQueue(CreateIdActivity.this).add(stringRequest);
    }

    private void addUser(String url, String userId, String username) {
        String apiKey = ApiKeyManager.getInstance().getApiKey();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("{")) {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                Toast.makeText(CreateIdActivity.this, message, Toast.LENGTH_SHORT).show();

                                if ("success".equals(status)) {
                                    // Handle success, if needed
                                } else {
                                    // Handle error, if needed
                                }
                            } else {
                                // Not a JSON object, handle as an error
                                Log.e(TAG, "Invalid JSON format in response: " + response);
                                Toast.makeText(CreateIdActivity.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateIdActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(CreateIdActivity.this, "Error submitting data", Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Insertid" ,"true");
                params.put("Userid", userId);
                params.put("Username", username);
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
        Volley.newRequestQueue(CreateIdActivity.this).add(stringRequest);
    }
}
