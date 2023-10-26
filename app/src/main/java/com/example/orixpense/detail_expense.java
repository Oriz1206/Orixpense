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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class detail_expense extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView deleteButton; // Textview for delete
    private TextView backButton; // Textview for back
    private Button editButton;

    private String expenseId;
    private DatabaseReference expenseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expense);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        // Get expense ID from Intent
        expenseId = getIntent().getStringExtra("expense_id");

        // Initialize DatabaseReference for the specific expense
        expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid).child(expenseId);

        // Map UI components
        balanceTextView = findViewById(R.id.Balance_detailEXP);
        categoryTextView = findViewById(R.id.cat_detailEXP);
        dateTextView = findViewById(R.id.detailEXP_date);
        descriptionTextView = findViewById(R.id.des_detailEXP);
        deleteButton = findViewById(R.id.btn_delete_detailEXP);
        backButton = findViewById(R.id.btn_back_detailEXP);
        editButton = findViewById(R.id.btn_edit_detailEXP);

        // Set up a ValueEventListener to fetch expense details
        expenseRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                String category = task.getResult().child("category").getValue(String.class);
                String date = task.getResult().child("date").getValue(String.class);
                String description = task.getResult().child("description").getValue(String.class);
                int amount = task.getResult().child("amount").getValue(Integer.class);

                // Display expense details
                categoryTextView.setText(category);
                dateTextView.setText(date);
                descriptionTextView.setText(description);
                balanceTextView.setText("-$" + amount); // Update the balance TextView for expenses
            } else {
                // Handle the case where the expense data is not found
                Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Handle the "Delete" button click
        deleteButton.setOnClickListener(v -> {
            // Create a confirmation dialog for deleting expense
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this expense?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Remove the expense from Firebase
                    expenseRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(detail_expense.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                            finish(); // Close the detail page
                        } else {
                            Toast.makeText(detail_expense.this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
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

        // Handle the "Edit" button click (Replace with your edit expense activity name)
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(detail_expense.this, expense_edit.class);
            editIntent.putExtra("expense_id", expenseId); // Pass the expense ID to the edit activity
            startActivity(editIntent);
        });
    }
}
