package com.example.orixpense;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;
    private RecyclerView irecyclerView;
    private RecyclerView erecyclerView;
    private FirebaseRecyclerAdapter<Data, ViewHolder> iadapter;
    private FirebaseRecyclerAdapter<Data, ViewHolder> eadapter;

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
        irecyclerView = view.findViewById(R.id.recycler_trans_INC);
        erecyclerView = view.findViewById(R.id.recycler_trans_EXP);

        LinearLayoutManager iLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager eLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        iLayoutManager.setReverseLayout(true);
        iLayoutManager.setStackFromEnd(true);
        eLayoutManager.setReverseLayout(true);
        eLayoutManager.setStackFromEnd(true);
        irecyclerView.setHasFixedSize(true);
        irecyclerView.setLayoutManager(iLayoutManager);
        erecyclerView.setHasFixedSize(true);
        erecyclerView.setLayoutManager(eLayoutManager);

        FirebaseRecyclerOptions<Data> ioptions = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDB, Data.class)
                .build();

        FirebaseRecyclerOptions<Data> eoptions = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDB, Data.class)
                .build();

        iadapter = new FirebaseRecyclerAdapter<Data, ViewHolder>(ioptions) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int position, Data model) {
                viewHolder.setiCat(model.getCategory());
                viewHolder.setiDes(model.getDescription());
                viewHolder.setiAmount(model.getAmount());
                viewHolder.setiDate(model.getDate());

                final int adapterPosition = position;

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), detail_income.class);
                        intent.putExtra("income_id", getRef(adapterPosition).getKey());
                        startActivity(intent);
                    }
                });

//                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        DatabaseReference itemRef = getRef(adapterPosition); // Lấy reference của khoản thu nhập
//                        itemRef.removeValue(); // Xóa khoản thu nhập từ Firebase
//                        return true;
//                    }
//                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recy_item, parent, false);
                return new ViewHolder(itemView);
            }
        };

        eadapter = new FirebaseRecyclerAdapter<Data, ViewHolder>(eoptions) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int position, Data model) {
                viewHolder.seteCat(model.getCategory());
                viewHolder.seteDes(model.getDescription());
                viewHolder.seteAmount(model.getAmount());
                viewHolder.seteDate(model.getDate());

                final int adapterPosition = position;

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), detail_expense.class);
                        intent.putExtra("expense_id", getRef(adapterPosition).getKey());
                        startActivity(intent);
                    }
                });

//                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        DatabaseReference itemRef = getRef(adapterPosition); // Lấy reference của khoản thu chi
//                        itemRef.removeValue(); // Xóa khoản thu chi từ Firebase
//                        return true;
//                    }
//                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recy_item, parent, false);
                return new ViewHolder(itemView);
            }
        };

        irecyclerView.setAdapter(iadapter);
        erecyclerView.setAdapter(eadapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (iadapter != null) {
            iadapter.startListening();
        }
        if (eadapter != null) {
            eadapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (iadapter != null) {
            iadapter.stopListening();
        }
        if (eadapter != null) {
            eadapter.stopListening();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setiCat(String icat) {
            TextView tiCat = mView.findViewById(R.id.cat_show_iINC);
            tiCat.setText(icat);
        }

        private void setiDes(String ides) {
            TextView tiDes = mView.findViewById(R.id.des_show_INC);
            tiDes.setText(ides);
        }

        private void setiAmount(int iamount) {
            TextView tiAmount = mView.findViewById(R.id.show_iINC);
            String strAmount = String.valueOf(iamount);
            tiAmount.setText("+$" + strAmount);
        }

        private void setiDate(String idate) {
            TextView tiDate = mView.findViewById(R.id.date_show_iINC);
            tiDate.setText(idate);
        }

        private void seteCat(String ecat) {
            TextView teCat = mView.findViewById(R.id.cat_show_iEXP);
            teCat.setText(ecat);
        }

        private void seteDes(String edes) {
            TextView teDes = mView.findViewById(R.id.des_show_EXP);
            teDes.setText(edes);
        }

        private void seteAmount(int eamount) {
            TextView teAmount = mView.findViewById(R.id.show_iEXP);
            String strAmount = String.valueOf(eamount);
            teAmount.setText("-$" + strAmount);
        }

        private void seteDate(String edate) {
            TextView teDate = mView.findViewById(R.id.date_show_iEXP);
            teDate.setText(edate);
        }
    }
}
