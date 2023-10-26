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

public class expense_edit extends AppCompatActivity {
    private EditText editAmount;
    private EditText editCategory;
    private EditText editDescription;
    private Button btnSaveEditExpense;
    private DatabaseReference expenseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_edit);

        // Nhận ID của khoản chi tiêu từ Intent
        String expenseId = getIntent().getStringExtra("expense_id");

        // Khởi tạo FirebaseAuth và DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid).child(expenseId);

        // Kết nối các thành phần giao diện
        editAmount = findViewById(R.id.edit_amount_EXP);
        editCategory = findViewById(R.id.cat_editEXP);
        editDescription = findViewById(R.id.des_editEXP);
        btnSaveEditExpense = findViewById(R.id.btn_save_editEXP);

        // Lấy thông tin khoản chi tiêu từ Firebase và hiển thị lên EditText
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String category = dataSnapshot.child("category").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    int amount = dataSnapshot.child("amount").getValue(Integer.class);

                    editCategory.setText(category);
                    editDescription.setText(description);
                    editAmount.setText(String.valueOf(amount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        btnSaveEditExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị mới từ EditText
                String newAmount = editAmount.getText().toString();
                String newCategory = editCategory.getText().toString();
                String newDescription = editDescription.getText().toString();

                // Kiểm tra xem các giá trị có hợp lệ không (ví dụ: newAmount là số dương)

                // Cập nhật dữ liệu lên Firebase
                expenseRef.child("amount").setValue(Integer.parseInt(newAmount));
                expenseRef.child("category").setValue(newCategory);
                expenseRef.child("description").setValue(newDescription);

                Toast.makeText(expense_edit.this, "Dữ liệu đã được cập nhật!", Toast.LENGTH_SHORT).show();

                finish(); // Sau khi cập nhật, đóng Activity
            }
        });
    }
}
