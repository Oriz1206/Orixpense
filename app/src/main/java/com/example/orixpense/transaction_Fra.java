package com.example.orixpense;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.orixpense.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class transaction_Fra extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Data, ViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        mIncomeDB = FirebaseDatabase.getInstance().getReference().child("Income").child(uid);
        mExpenseDB = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid);
        recyclerView = view.findViewById(R.id.recycler_trans);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDB, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int position, Data model) {
                viewHolder.setCat(model.getCategory());
                viewHolder.setDes(model.getDescription());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setDate(model.getDate());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
                return new ViewHolder(itemView);
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Log.d("FirebaseData", "Adapter started listening");
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setCat(String cat) {
            TextView tCat = mView.findViewById(R.id.cat_show);
            tCat.setText(cat);
        }

        private void setDes(String des) {
            TextView tDes = mView.findViewById(R.id.des_show);
            tDes.setText(des);
        }

        private void setAmount(int amount) {
            TextView tAmount = mView.findViewById(R.id.show);
            String strAmount = String.valueOf(amount);
            if (amount >= 0) {
                tAmount.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.green));
                tAmount.setText("+$" + String.valueOf(amount));
            } else {
                tAmount.setTextColor(ContextCompat.getColor(mView.getContext(), R.color.red));
                tAmount.setText("-$" + String.valueOf(-amount));
            }
        }

        private void setDate(String date) {
            TextView tDate = mView.findViewById(R.id.date_show);
            tDate.setText(date);
        }
    }
}
