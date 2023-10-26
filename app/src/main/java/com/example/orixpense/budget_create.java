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

public class budget_create extends AppCompatActivity {

    private EditText bamount;
    private EditText bdes;
    private Button btn_bsave;
    private Button btn_bcancle;

    private FirebaseAuth tAuth;
    private DatabaseReference BudgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_create);
        transferdata();
        tAuth = FirebaseAuth.getInstance();
        FirebaseUser iUser= tAuth.getCurrentUser();
        String uid=iUser.getUid();
        BudgetDB= FirebaseDatabase.getInstance().getReference().child("Budget").child(uid);
    }

    public void transferdata(){

        bamount = findViewById(R.id.amount_budget);
        bdes = findViewById(R.id.cat_addbudget);
        btn_bsave = findViewById(R.id.btn_save_addbudget);
        btn_bcancle = findViewById(R.id.btn_cancle_addBudget);


        btn_bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = bamount.getText().toString().trim();
                String description = bdes.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    bamount.setError("Required Field!");
                    return;
                }
                int amountInt = Integer.parseInt(amount);
                if (TextUtils.isEmpty(description)){
                    bdes.setError("Required Field!");
                    return;
                }
                String id = BudgetDB.push().getKey();
                String Date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amountInt,id,null,description,Date);

                BudgetDB.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show();
                    }
                });
                BudgetDB.child(id).setValue(data).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });
        btn_bcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}