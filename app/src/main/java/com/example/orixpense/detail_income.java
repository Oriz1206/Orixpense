package com.example.orixpense;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orixpense.R;
import com.example.orixpense.new_income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;

public class detail_income extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView deleteButton; // Textview for delete
    private TextView backButton; // Textview for back
    private Button editButton;

    private String incomeId;
    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_income);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        // Get income ID from Intent
        incomeId = getIntent().getStringExtra("income_id");

        // Initialize DatabaseReference for the specific income
        incomeRef = FirebaseDatabase.getInstance().getReference().child("Income").child(uid).child(incomeId);

        // Map UI components
        balanceTextView = findViewById(R.id.Balance_detailINC);
        categoryTextView = findViewById(R.id.cat_detailINC);
        dateTextView = findViewById(R.id.datailINC_date);
        descriptionTextView = findViewById(R.id.des_detailINC);
        deleteButton = findViewById(R.id.btn_delete_detailINC);
        backButton = findViewById(R.id.btn_back_detailINC);
        editButton = findViewById(R.id.btn_edit_detailINC);

        // Set up a ValueEventListener to fetch income details
        incomeRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                String category = task.getResult().child("category").getValue(String.class);
                String date = task.getResult().child("date").getValue(String.class);
                String description = task.getResult().child("description").getValue(String.class);
                int amount = task.getResult().child("amount").getValue(Integer.class);

                // Display income details
                categoryTextView.setText(category);
                dateTextView.setText(date);
                descriptionTextView.setText(description);
                balanceTextView.setText("$" + amount);
            } else {
                // Handle the case where the income data is not found
                Toast.makeText(this, "Income not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Handle the "Delete" button click
        deleteButton.setOnClickListener(v -> {
            // Create a confirmation dialog for deleting income
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this income?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Remove the income from Firebase
                    incomeRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(detail_income.this, "Income deleted", Toast.LENGTH_SHORT).show();
                            finish(); // Close the detail page
                        } else {
                            Toast.makeText(detail_income.this, "Failed to delete income", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        // Handle the "Back" button click
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Handle the "Edit" button click
        editButton.setOnClickListener(v -> {
            // Open the edit activity
            Intent editIntent = new Intent(detail_income.this, income_edit.class);
            editIntent.putExtra("income_id", incomeId); // Pass the income ID to the edit activity
            startActivity(editIntent);
        });
    }
}
