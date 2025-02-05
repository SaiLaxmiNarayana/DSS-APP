package com.example.dss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Intern extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button employeeWorkButton;
    private Button leaveRequestButton;
    private Button statusButton;
    private DrawerLayout drawerLayout;
    private ImageView profilePic;
    public static final String USERNAME_KEY="username";
    public static final String EMAIL_KEY="email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        profilePic = headerView.findViewById(R.id.profilePic);
        TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);

        String userName=getUserIdFromSharedPreferences();
        String Email=getuseremailFromSharedPreference();
        usernameTextView.setText(userName);
        emailTextView.setText(Email);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // You can set the initial state or do nothing if not using fragments
            navigationView.setCheckedItem(R.id.nav_home);
        }
        // Initialize buttons
        employeeWorkButton = findViewById(R.id.loginButton1);
        leaveRequestButton = findViewById(R.id.loginButton2);
        statusButton = findViewById(R.id.loginButton3);

        // Set onClickListeners for each button
        employeeWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EmployeeDailyWorkActivity when Employee Daily Work button is clicked
                Intent intent = new Intent(Intern.this, EmployeeDailyWorkActivity.class);
                startActivity(intent);
            }
        });

        leaveRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LeaveRequestActivity when Leave Request button is clicked
                Intent intent = new Intent(Intern.this, LeaveRequestActivity.class);
                startActivity(intent);
            }
        });

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start StatusActivity when Status button is clicked
                Intent intent = new Intent(Intern.this, StatusActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onProfilePicClick(View view) {
        // Open the gallery to select an image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            Toast.makeText(this, "Home is selected!", Toast.LENGTH_SHORT).show();
            // Add logic for handling the home action
        } else if (itemId == R.id.nav_share) {
            // Share the app
            shareApp();
        } else if (itemId == R.id.nav_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }
        else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            // Add logic for handling the logout action
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        // Clear the stored token
        clearToken();

        // Redirect to the login screen (MainActivity)
        Intent intent = new Intent(Intern.this, LoginActivity.class);
        startActivity(intent);

        // Finish the current activity
        finish();
    }



    private void clearToken() {
        // Clear the stored token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(LoginActivity.TOKEN_KEY);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the selected image URI from the intent
            Uri selectedImageUri = data.getData();

            // TODO: Implement logic to set the selected image
            if (selectedImageUri != null) {
                // Set the selected image to the profilePic ImageView
                profilePic.setImageURI(selectedImageUri);
            } else {
                // Handle the case where the selected image URI is null
                Toast.makeText(this, "Failed to retrieve selected image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app: https://play.google.com/store/apps/details?id=com.example.mouni");
        sendIntent.setType("text/plain");

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sendIntent, "Share App"));
        } else {
            Toast.makeText(this, "No app found to handle the share action", Toast.LENGTH_SHORT).show();
        }
    }
    private String getuseremailFromSharedPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(EMAIL_KEY, null);
    }
    private String getUserIdFromSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(USERNAME_KEY, null);
    }
}
