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

public class new_transfer extends AppCompatActivity {

    private EditText tamount;
    private EditText tdes;
    private Button btn_tsave;
    private Button btn_tcancle;

    private FirebaseAuth tAuth;
    private DatabaseReference TransferDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transfer);
        transferdata();
        tAuth = FirebaseAuth.getInstance();
        FirebaseUser iUser= tAuth.getCurrentUser();
        String uid=iUser.getUid();
        TransferDB= FirebaseDatabase.getInstance().getReference().child("Transfer").child(uid);
    }

    public void transferdata(){

        tamount = findViewById(R.id.add_amount_TRANS);
        tdes = findViewById(R.id.des_addTrans);
        btn_tsave = findViewById(R.id.btn_save_addTrans);
        btn_tcancle = findViewById(R.id.btn_cancle_addTrans);


        btn_tsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = tamount.getText().toString().trim();
                String description = tdes.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    tamount.setError("Required Field!");
                    return;
                }
                int amountInt = Integer.parseInt(amount);
                if (TextUtils.isEmpty(description)){
                    tdes.setError("Required Field!");
                    return;
                }
                String id = TransferDB.push().getKey();
                String Date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amountInt,id,null,description,Date);

                TransferDB.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Added!",Toast.LENGTH_SHORT).show();
                    }
                });
                TransferDB.child(id).setValue(data).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                finish();

            }
        });
        btn_tcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}