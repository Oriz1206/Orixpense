package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class nav extends AppCompatActivity {

    private FloatingActionButton btn_ft_main;
    private FloatingActionButton btn_ft_INC;
    private FloatingActionButton btn_ft_EXP;
    private FloatingActionButton btn_ft_Trans;
    private TextView ft_INC;
    private TextView ft_EXP;
    private TextView ft_Trans;
    private boolean isOpen = false;
    private Animation F_open, F_close;

    //nav
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private home_Fra homeFra;
    private budget_Fra budgetFra;
    private transaction_Fra transactionFra;
    private profile_Fra profileFra;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        btn_ft_main = findViewById(R.id.ft_main);
        btn_ft_INC = findViewById(R.id.btn_ft_INC);
        btn_ft_EXP = findViewById(R.id.btn_ft_EXP);
        btn_ft_Trans = findViewById(R.id.btn_ft_Trans);

        ft_INC = findViewById(R.id.INC_ft);
        ft_EXP = findViewById(R.id.EXP_ft);
        ft_Trans = findViewById(R.id.Trans_ft);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.fragment_container);

        F_open = AnimationUtils.loadAnimation(this, R.anim.fade_open);
        F_close = AnimationUtils.loadAnimation(this, R.anim.fade_close);

        btn_ft_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    btn_ft_INC.startAnimation(F_close);
                    btn_ft_EXP.startAnimation(F_close);
                    btn_ft_Trans.startAnimation(F_close);
                    btn_ft_INC.setClickable(false);
                    btn_ft_EXP.setClickable(false);
                    btn_ft_Trans.setClickable(false);

                    ft_INC.startAnimation(F_close);
                    ft_EXP.startAnimation(F_close);
                    ft_Trans.startAnimation(F_close);
                    ft_INC.setClickable(false);
                    ft_EXP.setClickable(false);
                    ft_Trans.setClickable(false);
                    isOpen = false;
                } else {
                    btn_ft_INC.startAnimation(F_open);
                    btn_ft_EXP.startAnimation(F_open);
                    btn_ft_Trans.startAnimation(F_open);
                    btn_ft_INC.setClickable(true);
                    btn_ft_EXP.setClickable(true);
                    btn_ft_Trans.setClickable(true);

                    ft_INC.startAnimation(F_open);
                    ft_EXP.startAnimation(F_open);
                    ft_Trans.startAnimation(F_open);
                    ft_INC.setClickable(true);
                    ft_EXP.setClickable(true);
                    ft_Trans.setClickable(true);
                    isOpen = true;
                }
            }
        });
        btn_ft_INC.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),new_income.class)));
        btn_ft_EXP.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),New_Expense.class)));
        btn_ft_Trans.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),new_transfer.class)));

        homeFra = new home_Fra();
        transactionFra = new transaction_Fra();
        budgetFra = new budget_Fra();
        profileFra = new profile_Fra();
        setFragment(homeFra);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home_nav) {
                    setFragment(homeFra);
                    btn_ft_main.setVisibility(View.VISIBLE);
                    btn_ft_INC.setVisibility(View.VISIBLE);
                    btn_ft_EXP.setVisibility(View.VISIBLE);
                    btn_ft_Trans.setVisibility(View.VISIBLE);
                    ft_INC.setVisibility(View.VISIBLE);
                    ft_EXP.setVisibility(View.VISIBLE);
                    ft_Trans.setVisibility(View.VISIBLE);

                    return true;
                } else if (itemId == R.id.transaction_nav) {
                    setFragment(transactionFra);
                    btn_ft_main.setVisibility(View.VISIBLE);
                    btn_ft_INC.setVisibility(View.VISIBLE);
                    btn_ft_EXP.setVisibility(View.VISIBLE);
                    btn_ft_Trans.setVisibility(View.VISIBLE);
                    ft_INC.setVisibility(View.VISIBLE);
                    ft_EXP.setVisibility(View.VISIBLE);
                    ft_Trans.setVisibility(View.VISIBLE);
                    return true;
                } else if (itemId == R.id.budget_nav) {
                    setFragment(budgetFra);
                    btn_ft_main.setVisibility(View.GONE);
                    btn_ft_INC.setVisibility(View.GONE);
                    btn_ft_EXP.setVisibility(View.GONE);
                    btn_ft_Trans.setVisibility(View.GONE);
                    ft_INC.setVisibility(View.GONE);
                    ft_EXP.setVisibility(View.GONE);
                    ft_Trans.setVisibility(View.GONE);
                    return true;
                } else if (itemId == R.id.profile_nav) {
                    setFragment(profileFra);
                    btn_ft_main.setVisibility(View.GONE);
                    btn_ft_INC.setVisibility(View.GONE);
                    btn_ft_EXP.setVisibility(View.GONE);
                    btn_ft_Trans.setVisibility(View.GONE);
                    ft_INC.setVisibility(View.GONE);
                    ft_EXP.setVisibility(View.GONE);
                    ft_Trans.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }



}
