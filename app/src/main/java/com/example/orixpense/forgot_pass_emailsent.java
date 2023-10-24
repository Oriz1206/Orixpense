package com.example.orixpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class forgot_pass_emailsent extends AppCompatActivity {

    private Button back_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_emailsent);
        emailsent();
    }

    protected void emailsent(){
        back_login = findViewById(R.id.btn_back_to_login);
        back_login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Onboarding_login_Activity.class)));

    }
}