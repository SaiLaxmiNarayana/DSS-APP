// AboutActivity.java
package com.example.dss;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up UI elements
        TextView titleTextView = findViewById(R.id.aboutTitle);
        TextView descriptionTextView = findViewById(R.id.aboutDescription);

        // Set information about the app
        titleTextView.setText("About App");
        descriptionTextView.setText("The Leave Management app efficiently handles employee leave requests, automating the entire process from submission to approval. With a user-friendly interface, employees can easily request leave, track their balances, and receive timely approvals. Managers benefit from streamlined leave management, reducing administrative burden. The app ensures accurate leave records, enhances transparency, and ultimately improves workforce productivity");

        // Add more information or customize the UI as needed
    }
}
