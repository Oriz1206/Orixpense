package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class new_income extends AppCompatActivity {

    private EditText iamount;
    private EditText icat;
    private EditText ides;
    private Button btn_isave;
    private Button btn_icancle;

    private FirebaseAuth iAuth;
    private DatabaseReference IncomeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);
        incomedata();
        iAuth = FirebaseAuth.getInstance();
        FirebaseUser iUser= iAuth.getCurrentUser();
        String uid=iUser.getUid();
        IncomeDB= FirebaseDatabase.getInstance().getReference().child("Income").child(uid);
    }

    public void incomedata(){
        iamount = findViewById(R.id.add_amount_INC);
        icat = findViewById(R.id.cat_addINC);
        ides = findViewById(R.id.des_addINC);
        btn_isave = findViewById(R.id.btn_save_addINC);
        btn_icancle = findViewById(R.id.btn_cancle_addINC);

        btn_isave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = iamount.getText().toString().trim();
                String category = icat.getText().toString().trim();
                String description = ides.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    iamount.setError("Required Field!");
                    return;
                }
                int amountInt = Integer.parseInt(amount);
                if (TextUtils.isEmpty(category)){
                    icat.setError("Required Field!");
                    return;
                }
                if (TextUtils.isEmpty(description)){
                    ides.setError("Required Field!");
                    return;
                }
                String id = IncomeDB.push().getKey();
                String Date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amountInt,id,category,description,Date);

                IncomeDB.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show();
                    }
                });
                IncomeDB.child(id).setValue(data).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });

        btn_icancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}