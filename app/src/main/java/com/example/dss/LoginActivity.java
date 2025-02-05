package com.example.dss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.airbnb.lottie.LottieAnimationView;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private LottieAnimationView loadingAnimationView;
    private ALoadingDialog aLodingDialog;

    private static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String TOKEN_KEY = "token";
    public  static  final String USER_ROLE = "userRole" ;
    public  static final String USERNAME_KEY="username";
    // Add these declarations at the beginning of performLogin method
   //  public String username;
    TextView alreadySignup,forgetPassword;
    SharedPreferences preference;

    // public String userRole ;


    public static final String USERID_KEY = "userid";
    public static final String EMAIL_KEY="email";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        aLodingDialog = new ALoadingDialog(this);
        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        // Check if a token exists in SharedPreferences
       String storedToken = getTokenFromSharedPreferences();
        String storedrole = getroleFromSharedPreferences();

       if (storedToken != null && !storedToken.isEmpty()) {


            Log.d("MainActivity", "Username: " + storedToken);
            Log.d("MainActivity", "Username: " + storedrole);



            if ("CEO".equals(storedrole)) {
                startActivity(new Intent(getBaseContext(), CEO.class));
            } else if ("INTERN".equals(storedrole)) {
                startActivity(new Intent(getBaseContext(), Intern.class));
            } else if ("EMPLOYEE".equals(storedrole)) {
                startActivity(new Intent(getBaseContext(), Intern.class));
            } else {
                // Default navigation or handle other roles
            }
            finish();


        }   Log.d(TAG, "User is already logged in");


        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        aLodingDialog = new ALoadingDialog(this);
        alreadySignup = findViewById(R.id.signupText) ;
        forgetPassword=findViewById(R.id.fp);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the username and password from the EditText fields
                String userid = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                aLodingDialog.show();

                // Call a method to perform the login/authentication
                performLogin(userid, password);
            }
        });
        alreadySignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,sendotp.class));
            }
        });


    }

    private void performLogin(final String userid, final String password) {
        // animation
        //loadingAnimationView.setVisibility(View.VISIBLE);

        String apiKey = ApiKeyManager.getInstance().getApiKey();



        // Specify your PHP backend URL
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "Overall.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Raw response: " + response);
                        try {
                            JSONObject responseJson = new JSONObject(response);

                            if (responseJson.has("status")) {
                                String status = responseJson.getString("status");

                                if ("success".equals(status)) {
                                    // Successful login

                                 //   useridpassing.setUserid(userid);

                                    saveUserIdToSharedPreferences(userid);

                                  String   userRole = responseJson.getString("user_role") ;
                                    UserManager.setUserRole(userRole);
                                    String token = responseJson.getString("token");
                                    saveTokenToSharedPreferences(token ,userRole);
                                    String username = responseJson.getString("username");
                                    String email = responseJson.getString("email");
                                    saveUserNameToSharedPreference(username,email);

                                    //          String username = responseJson.getString("username");
                                    //String password = responseJson.getString("password");
                                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                    Log.d("HomeActivity", "Retrieved token: " + token);

                                    //saveUserCredentials(username);

                                    // Navigate based on user role
                                    if ("CEO".equals(userRole)) {
                                        startActivity(new Intent(getBaseContext(), CEO.class));
                                    } else if ("INTERN".equals(userRole)) {
                                        startActivity(new Intent(getBaseContext(), Intern.class));
                                    } else if ("EMPLOYEE".equals(userRole)) {
                                        startActivity(new Intent(getBaseContext(), Intern.class));
                                    } else {
                                        // Default navigation or handle other roles
                                    }
                                    aLodingDialog.cancel();

                                   finish();

                                } else {

                                    String message = responseJson.optString("message", "Login failed");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle missing "status" key in JSON response
                                aLodingDialog.cancel();
                                Log.e(TAG, "No 'status' key found in the server response");
                                Toast.makeText(getApplicationContext(), "Error parsing server response", Toast.LENGTH_SHORT).show();

                            }
//                            loadingAnimationView.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            //Toast.makeText(getApplicationContext(), "Error parsing server response", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Dismiss the loading animation
                aLodingDialog.cancel();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Enter Valid Userid or Password", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", "true");
                params.put("userid", userid);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + apiKey);
                return params;
            }
        };

        // To prevent timeout error
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(80000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void saveUserNameToSharedPreference(String username,String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.putString(EMAIL_KEY, email);

        editor.apply();
    }

   /* private void saveUserCredentials(String username) {
        // Save the user credentials to SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        //editor.putString("password", password);
        editor.apply();
    }*/

    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN_KEY, null);
    }

    private String getroleFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_ROLE, null);
    }

    private void saveTokenToSharedPreferences(String token ,String userRole) {
        // Save the token securely using SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN_KEY, token );
        editor.putString(USER_ROLE, userRole );
        editor.apply();
    }

    private void saveUserIdToSharedPreferences(String userid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERID_KEY, userid);
        editor.apply();
    }

}
