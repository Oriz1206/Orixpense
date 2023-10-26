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

public class income_edit extends AppCompatActivity {
    private EditText editAmount;
    private EditText editCategory;
    private EditText editDescription;
    private Button btnSaveEditIncome;
    private DatabaseReference incomeRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_edit);

        // Nhận ID của khoản thu nhập từ Intent
        String incomeId = getIntent().getStringExtra("income_id");

        // Khởi tạo FirebaseAuth và DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        incomeRef = FirebaseDatabase.getInstance().getReference().child("Income").child(uid).child(incomeId);

        // Kết nối các thành phần giao diện
        editAmount = findViewById(R.id.edit_amount_INC);
        editCategory = findViewById(R.id.cat_editINC);
        editDescription = findViewById(R.id.edit_addINC);
        btnSaveEditIncome = findViewById(R.id.btn_save_editINC);

        // Lấy thông tin khoản thu nhập từ Firebase và hiển thị lên EditText
        incomeRef.addValueEventListener(new ValueEventListener() {
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

        btnSaveEditIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị mới từ EditText
                String newAmount = editAmount.getText().toString();
                String newCategory = editCategory.getText().toString();
                String newDescription = editDescription.getText().toString();

                // Kiểm tra xem các giá trị có hợp lệ không (ví dụ: newAmount là số dương)

                // Cập nhật dữ liệu lên Firebase
                incomeRef.child("amount").setValue(Integer.parseInt(newAmount));
                incomeRef.child("category").setValue(newCategory);
                incomeRef.child("description").setValue(newDescription);

                Toast.makeText(income_edit.this, "Dữ liệu đã được cập nhật!", Toast.LENGTH_SHORT).show();

                finish(); // Sau khi cập nhật, đóng Activity
            }
        });
    }
}
