package com.example.orixpense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.orixpense.Model.Data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class home_Fra extends Fragment {
    private TextView totalIncome;
    private TextView totalExpense;
    private TextView balance;
    private TextView btn_see_all;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;
    private FirebaseAuth mAuth;
    private int INC;
    private int EXP;

    private TextView B_EXP;
    private TextView B_INC;
    private TextView Des_EXP;
    private TextView Des_INC;
    private TextView Cat_EXP;
    private TextView Cat_INC;
    private TextView Date_EXP;
    private TextView Date_INC;


    public home_Fra() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        totalIncome = view.findViewById(R.id.home_INC);
        totalExpense = view.findViewById(R.id.home_EXP);
        balance = view.findViewById(R.id.Balance);
        btn_see_all = view.findViewById(R.id.see_all_trans);
        B_EXP = view.findViewById(R.id.home_iEXP);
        Des_EXP= view.findViewById(R.id.des_home_iEXP);
        Cat_EXP= view.findViewById(R.id.cat_home_iEXP);
        Date_EXP= view.findViewById(R.id.date_home_iEXP);
        B_INC = view.findViewById(R.id.home_iINC);
        Des_INC= view.findViewById(R.id.des_home_iINC);
        Cat_INC= view.findViewById(R.id.cat_home_iINC);
        Date_INC= view.findViewById(R.id.date_home_iINC);
//        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.bottom_nav);
//        NavController navController = navHostFragment.getNavController();

//        btn_see_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển đến fragment_transaction khi nút "See All" được nhấn
//                navController.navigate(R.id.transaction_nav);
//            }
//        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mIncomeDB = FirebaseDatabase.getInstance().getReference().child("Income").child(uid);
        mExpenseDB = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid);

        mIncomeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum = 0;
                for(DataSnapshot mysnap:snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    totalsum += data.getAmount();
                    String stResult = String.valueOf(totalsum);
                    totalIncome.setText("$" + String.valueOf(stResult));
                    INC = totalsum;
                    updateBalance();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mExpenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum = 0;
                for(DataSnapshot mysnap:snapshot.getChildren()){
                    Data data = mysnap.getValue(Data.class);
                    totalsum += data.getAmount();
                    String stResult = String.valueOf(totalsum);
                    totalExpense.setText("$" + String.valueOf(stResult));
                    EXP = totalsum;
                    updateBalance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mIncomeDB.orderByChild("date").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    Data data = childSnapshot.getValue(Data.class);
                    if (data != null) {
                        // Update your UI elements with the latest income data
                        B_INC.setText("$" + data.getAmount());
                        Des_INC.setText(data.getDescription());
                        Cat_INC.setText(data.getCategory());
                        Date_INC.setText(data.getDate());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDB.orderByChild("date").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    Data data = childSnapshot.getValue(Data.class);
                    if (data != null) {
                        B_EXP.setText("$" + data.getAmount());
                        Des_EXP.setText(data.getDescription());
                        Cat_EXP.setText(data.getCategory());
                        Date_EXP.setText(data.getDate());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
    private void updateBalance() {
        if (getContext() != null) {
            if ((INC - EXP) >= 0) {
                balance.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else {
                balance.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }
            balance.setText("$" + String.valueOf(INC - EXP));
        }
    }

}