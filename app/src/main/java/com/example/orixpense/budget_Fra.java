package com.example.orixpense;

import android.content.Intent;
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

public class budget_Fra extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference BudgetDB;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    private TextView btn_create_budget;


    public budget_Fra() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_budget_, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        BudgetDB = FirebaseDatabase.getInstance().getReference().child("Budget").child(uid);

        recyclerView = view.findViewById(R.id.view_budget);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        btn_create_budget = view.findViewById(R.id.btn_create_budget);
        btn_create_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), budget_create.class);
                startActivity(intent);
            }
        });

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(BudgetDB, Data.class).build();

        adapter =new FirebaseRecyclerAdapter<Data, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_budget, parent, false);


                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Data model) {
                holder.setCat(model.getCategory());
                holder.setAmount(model.getAmount());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), budget_detail.class);
                        intent.putExtra("budget_id", getRef(position).getKey()); // Truyền ID của ngân sách
                        startActivity(intent);
                    }
                });


            }
        };
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        private void setCat(String cat) {
            TextView tCat = mView.findViewById(R.id.cat_budget);
            tCat.setText(cat);
        }

        private void setAmount(int amount) {
            TextView tAmount = mView.findViewById(R.id.budget_show);
            tAmount.setText("Remaining $" + String.valueOf(amount));
        }


    }

    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }


}