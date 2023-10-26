package com.example.orixpense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.orixpense.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_Fra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_Fra extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //total
    private TextView totalIncome;
    private TextView totalExpense;
    private TextView balance;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;
    private FirebaseAuth mAuth;
    private int INC;
    private int EXP;

    public home_Fra() {
        // Required empty public constructor
    }


    public static home_Fra newInstance(String param1, String param2) {
        home_Fra fragment = new home_Fra();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        totalIncome = view.findViewById(R.id.home_INC);
        totalExpense = view.findViewById(R.id.home_EXP);
        balance = view.findViewById(R.id.Balance);


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