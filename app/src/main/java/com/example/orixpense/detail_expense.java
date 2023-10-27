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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detail_expense extends AppCompatActivity {

    private TextView balanceTextView;
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView deleteButton;
    private TextView backButton;
    private Button editButton;

    private String expenseId;
    private DatabaseReference expenseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_expense);

        expenseId = getIntent().getStringExtra("expense_id");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid).child(expenseId);

        balanceTextView = findViewById(R.id.Balance_detailEXP);
        categoryTextView = findViewById(R.id.cat_detailEXP);
        dateTextView = findViewById(R.id.detailEXP_date);
        descriptionTextView = findViewById(R.id.des_detailEXP);
        deleteButton = findViewById(R.id.btn_delete_detailEXP);
        backButton = findViewById(R.id.btn_back_detailEXP);
        editButton = findViewById(R.id.btn_edit_detailEXP);

        expenseRef.addValueEventListener(new ValueEventListener() {
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
                    balanceTextView.setText("-$" + amount);

                    deleteButton.setOnClickListener(v ->                                                                                                                {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_expense.this);
                        builder.setTitle("Confirm Delete");
                        builder.setMessage("Are you sure you want to delete this expense?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expenseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(detail_expense.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                        Toast.makeText(detail_expense.this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                });
                            }


                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    });

                    editButton.setOnClickListener(v -> {
                        Intent editIntent = new Intent(detail_expense.this, expense_edit.class);
                        editIntent.putExtra("expense_id", expenseId);
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
