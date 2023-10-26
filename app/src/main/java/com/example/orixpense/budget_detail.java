package com.example.orixpense;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class budget_detail extends AppCompatActivity {


    private String budgetId;
    private DatabaseReference budgetRef;
    private TextView detailCatBudget, detailBudget,btnDeleteBudget;
    private Button btnEditBudget;
    private FirebaseAuth mAuth;
    private TextView btn_back_budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_detail);

        // Nhận ID của ngân sách từ Intent
        budgetId = getIntent().getStringExtra("budget_id");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        // Khởi tạo DatabaseReference cho ngân sách cụ thể
        budgetRef = FirebaseDatabase.getInstance().getReference().child("Budget").child(uid).child(budgetId);
        btn_back_budget = findViewById(R.id.btn_back_detail_budget);
        btn_back_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Kết nối các thành phần giao diện
        detailCatBudget = findViewById(R.id.detail_cat_budget);
        detailBudget = findViewById(R.id.detail_budget);
        btnEditBudget = findViewById(R.id.btn_edit_budget);
        btnDeleteBudget = findViewById(R.id.btn_delete_detail_budget);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String category = dataSnapshot.child("category").getValue(String.class);
                    int amount = dataSnapshot.child("amount").getValue(Integer.class);

                    detailCatBudget.setText(category);
                    detailBudget.setText("$" + amount);

                    btnEditBudget.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), budget_edit.class);
                            intent.putExtra("budget_id", budgetId); // Truyền ID của ngân sách
                            startActivity(intent);
                        }
                    });

                    btnDeleteBudget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Tạo một hộp thoại xác nhận xóa
                            AlertDialog.Builder builder = new AlertDialog.Builder(budget_detail.this);
                            builder.setMessage("Bạn có chắc chắn muốn xóa ngân sách này?");
                            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    budgetRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                finish();
                                            } else {
                                                makeText(budget_detail.this, "", Toast.LENGTH_SHORT).show();makeText(budget_detail.this, "Lỗi khi xóa ngân sách", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Hủy", null);
                            builder.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
