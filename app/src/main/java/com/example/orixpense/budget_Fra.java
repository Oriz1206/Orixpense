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
    private boolean dataReceived = false;

    private TextView btn_create_budget;


    public budget_Fra() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("budget_Fra", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_budget_, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = "7HfvCXWJsOfn6uGhe5VDKdohPIm2";
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("budget_Fra", "onStart called");

        BudgetDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataReceived = true;
                    Log.d("Budget_Fra", "Dữ liệu đã được nhận từ Firebase" + String.valueOf(dataSnapshot));
                } else {
                    dataReceived = false;
                    Log.d("Budget_Fra", "Dữ liệu không được nhận từ Firebase");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dataReceived = false;
                Log.d("Budget_Fra", "Dữ liệu không được nhận từ Firebase");
            }
        });

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(BudgetDB, Data.class).build();

        adapter =new FirebaseRecyclerAdapter<Data, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_budget, parent, false);
                Log.d("Budget_Fra", "ViewHolder onCreateViewHolder");
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Data model) {
                // Bind data to the ViewHolder
                holder.setCat(model.getCategory()); // Assuming you have a getCat method in your Data class
                holder.setAmount(model.getAmount()); // Assuming you have a getAmount method in your Data class
                Log.d("YourAdapter", "onBindViewHolder called for position: " + position);
                Log.d("YourAdapter", "Data for this position - Category: " + model.getCategory() + ", Amount: " + model.getAmount());
            }
        };


        Log.d("budget_Fra", "FirebaseRecyclerAdapter created");
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        if (recyclerView.getAdapter() != null) {
            Log.d("Budget_Fra", "Adapter is set to the RecyclerView"+ recyclerView.getAdapter());
            Log.d("Budget_Fra", "Adapter "+ adapter);
        } else {
            Log.d("Budget_Fra", "Adapter is not set to the RecyclerView"+recyclerView.getAdapter());
        }



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


    ////
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening(); // Stop listening when the fragment is no longer visible.
        }
    }


}