package com.example.orixpense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.orixpense.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class transaction_Fra extends Fragment {

    private FirebaseAuth tAuth;
    private DatabaseReference tIncomeDB;
    private DatabaseReference tExpenseDB;
    private RecyclerView irecyclerView;

    private RecyclerView erecyclerView;



    private FirebaseRecyclerAdapter<Data, viewHolder> iadapter;
    private FirebaseRecyclerAdapter<Data, viewHolder> eadapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tView = inflater.inflate(R.layout.fragment_transaction_, container, false);

        tAuth = FirebaseAuth.getInstance();
        FirebaseUser tUser= tAuth.getCurrentUser();
        String uid= tUser.getUid();

        tIncomeDB = FirebaseDatabase.getInstance().getReference().child("Income").child(uid);
        tExpenseDB = FirebaseDatabase.getInstance().getReference().child("Expense").child(uid);
        irecyclerView = tView.findViewById(R.id.recycler_id);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        irecyclerView.setHasFixedSize(true);
        irecyclerView.setLayoutManager(layoutManager);

        return tView;
    }

    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<Data> ioptions = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(tIncomeDB, Data.class)
                .build();

        FirebaseRecyclerOptions<Data> eoptions = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(tExpenseDB, Data.class)
                .build();

        iadapter = new FirebaseRecyclerAdapter<Data, viewHolder>(ioptions) {
            @Override
            protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Data model) {
                holder.setCat(model.getCategory());
                holder.setDes(model.getDescription());
                holder.setDate(model.getDate());
                holder.setAmount(model.getAmount());
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recy_item, parent, false));
            }
        };

        irecyclerView.setAdapter(iadapter);
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        View mView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setCat (String cat){
            TextView tCat = mView.findViewById(R.id.cat_show_INC);
            tCat.setText(cat);
        }
        private void setDes (String des){
            TextView tDes = mView.findViewById(R.id.des_show_INC);
            tDes.setText(des);
        }
        private void setDate (String date){
            TextView tDate = mView.findViewById(R.id.date_show_INC);
            tDate.setText(date);
        }
        private void setAmount (int amount){
            TextView tAmount = mView.findViewById(R.id.show_INC);
            String strAmount = String.valueOf(amount);
            tAmount.setText(amount);
        }
    }

}