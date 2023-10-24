package com.example.orixpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnboardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        Intent ihome = new Intent(OnboardActivity.this, onboarding_2_Activity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(ihome);
            }
        },1000);


    }

}