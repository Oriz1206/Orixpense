package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class budget_edit extends AppCompatActivity {
    private EditText editBudget;
    private EditText editCategory;
    private Button btnSaveEditBudget;
    private DatabaseReference budgetRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);

        // Nhận ID của ngân sách từ Intent
        String budgetId = getIntent().getStringExtra("budget_id");

        // Khởi tạo FirebaseAuth và DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("Budget").child(uid).child(budgetId);

        // Kết nối các thành phần giao diện
        editBudget = findViewById(R.id.edit_budget);
        editCategory = findViewById(R.id.cat_editbudget);
        btnSaveEditBudget = findViewById(R.id.btn_save_editbudget);

        // Lấy thông tin ngân sách từ Firebase và hiển thị lên EditText
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String category = dataSnapshot.child("category").getValue(String.class);
                    int amount = dataSnapshot.child("amount").getValue(Integer.class);

                    editCategory.setText(category);
                    editBudget.setText(String.valueOf(amount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        btnSaveEditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị mới từ EditText
                String newBudget = editBudget.getText().toString();
                String newCategory = editCategory.getText().toString();

                // Kiểm tra xem các giá trị có hợp lệ không (ví dụ: newBudget là số dương)

                // Cập nhật dữ liệu lên Firebase
                budgetRef.child("amount").setValue(Integer.parseInt(newBudget));
                budgetRef.child("category").setValue(newCategory);

                Toast.makeText(budget_edit.this, "Dữ liệu đã được cập nhật!", Toast.LENGTH_SHORT).show();

                finish(); // Sau khi cập nhật, đóng Activity
            }
        });
    }
}
