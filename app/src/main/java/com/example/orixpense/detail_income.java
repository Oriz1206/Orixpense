package com.example.orixpense;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detail_income extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView deleteButton;
    private TextView backButton;
    private Button editButton;

    private String incomeId;
    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_income);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        incomeId = getIntent().getStringExtra("income_id");

        incomeRef = FirebaseDatabase.getInstance().getReference().child("Income").child(uid).child(incomeId);

        balanceTextView = findViewById(R.id.Balance_detailINC);
        categoryTextView = findViewById(R.id.cat_detailINC);
        dateTextView = findViewById(R.id.datailINC_date);
        descriptionTextView = findViewById(R.id.des_detailINC);
        deleteButton = findViewById(R.id.btn_delete_detailINC);
        backButton = findViewById(R.id.btn_back_detailINC);
        editButton = findViewById(R.id.btn_edit_detailINC);

        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String category = snapshot.child("category").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    int amount = snapshot.child("amount").getValue(Integer.class);

                    categoryTextView.setText(category);
                    dateTextView.setText(date);
                    descriptionTextView.setText(description);
                    balanceTextView.setText("$" + amount);

                    deleteButton.setOnClickListener(v -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_income.this);
                        builder.setTitle("Confirm Delete");
                        builder.setMessage("Are you sure you want to delete this income?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                incomeRef.removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(detail_income.this, "Income deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(detail_income.this, "Failed to delete income", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    });

                    editButton.setOnClickListener(v -> {
                        Intent editIntent = new Intent(detail_income.this, income_edit.class);
                        editIntent.putExtra("income_id", incomeId);
                        startActivity(editIntent);
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backButton.setOnClickListener(v -> {
            finish();
        });


    }
}
