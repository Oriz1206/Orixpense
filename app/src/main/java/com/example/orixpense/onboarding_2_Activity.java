package com.example.orixpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class onboarding_2_Activity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding2);
        onboarding2();
    }

    protected void onboarding2(){
        btn_signup = findViewById(R.id.btn_ob2_signup);
        btn_login = findViewById(R.id.btn_ob2_login);

        btn_signup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Onboarding_signup_Activity.class)));

        btn_login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Onboarding_login_Activity.class)));

    }
}