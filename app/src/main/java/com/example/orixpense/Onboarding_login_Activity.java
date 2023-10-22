package com.example.orixpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Onboarding_login_Activity extends AppCompatActivity {

    private EditText email_login;
    private EditText pass_login;
    private Button btnlogin;
    private TextView dont_have_acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_login);

        login();

    }

    private void login(){

        email_login = findViewById(R.id.email_login);
        pass_login = findViewById(R.id.password_login);
        btnlogin = findViewById(R.id.btnlogin);
        dont_have_acc = findViewById(R.id.dont_have_acc);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = email_login.getText().toString().trim();
                String pass= pass_login.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    email_login.setError("Email is required!");
                }

                if (TextUtils.isEmpty(pass)){
                    pass_login.setError("Password is required!");
                }

            }
        });

    }}