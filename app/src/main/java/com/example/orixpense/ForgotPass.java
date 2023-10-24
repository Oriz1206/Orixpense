package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    private EditText fEmail;
    private Button Fcontinue;
    private FirebaseAuth F_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        F_pass = FirebaseAuth.getInstance();
        fogotpass();
    }

    protected void fogotpass(){
        fEmail = findViewById(R.id.sent_email);
        Fcontinue = findViewById(R.id.btn_continue_forgotP);


        Fcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = fEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    fEmail.setError("Email is required!");
                    return;
                }

                F_pass.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),forgot_pass_emailsent.class));
                        }else{
                            Toast.makeText(getApplicationContext(), "Email sent fail!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}