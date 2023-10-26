package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orixpense.Model.Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class New_Expense extends AppCompatActivity {

    private EditText eamount;
    private EditText ecat;
    private EditText edes;
    private Button btn_esave;
    private Button btn_ecancle;
    private TextView btn_back_addEXP;


    private FirebaseAuth eAuth;
    private DatabaseReference ExpneseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        expnesedata();
        eAuth = FirebaseAuth.getInstance();
        FirebaseUser iUser= eAuth.getCurrentUser();
        String uid=iUser.getUid();
        ExpneseDB= FirebaseDatabase.getInstance().getReference().child("Expense").child(uid);

        btn_back_addEXP = findViewById(R.id.btn_back_addEXP);
        btn_back_addEXP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng màn hình hiện tại và quay lại màn hình trước
            }
        });
    }

    public void expnesedata(){
        eamount = findViewById(R.id.add_amount_EXP);
        ecat = findViewById(R.id.cat_addEXP);
        edes = findViewById(R.id.des_addEXP);
        btn_esave = findViewById(R.id.btn_save_addEXP);
        btn_ecancle = findViewById(R.id.btn_cancle_addEXP);
        eamount.setInputType(InputType.TYPE_CLASS_NUMBER);


        btn_esave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = eamount.getText().toString().trim();
                String category = ecat.getText().toString().trim();
                String description = edes.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    eamount.setError("Required Field!");
                    return;
                }
                int amountInt = Integer.parseInt(amount);
                if (TextUtils.isEmpty(category)){
                    ecat.setError("Required Field!");
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    edes.setError("Required Field!");
                    return;
                }
                String id = ExpneseDB.push().getKey();
                String Date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amountInt,id,category,description,Date);

                ExpneseDB.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show();
                    }
                });
                ExpneseDB.child(id).setValue(data).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });
        btn_ecancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}