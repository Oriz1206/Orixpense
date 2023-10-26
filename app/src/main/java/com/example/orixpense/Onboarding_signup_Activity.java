package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Onboarding_signup_Activity extends AppCompatActivity {

    private EditText name_signup;
    private EditText email_signup;
    private EditText pass_signup;
    private Button btnsignup;
    private TextView have_acc;
    private TextView btn_back_signup;
    private ProgressDialog signup_dialog;

    //firebase
    private FirebaseAuth signup_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_signup);
        signup_auth = FirebaseAuth.getInstance();
        signup_dialog = new ProgressDialog(this);
        signup();
    }

    protected void signup(){
        name_signup = findViewById(R.id.name_signup);
        email_signup = findViewById(R.id.email_signup);
        pass_signup = findViewById(R.id.pass_signup);
        btnsignup = findViewById(R.id.btnsignup);
        have_acc = findViewById(R.id.have_acc);
        btn_back_signup = findViewById(R.id.btn_back_signup);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_signup.getText().toString().trim();
                String email = email_signup.getText().toString().trim();
                String pass= pass_signup.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    name_signup.setError("Username is required!");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    email_signup.setError("Email is required!");
                    return;
                } else if (!isEmailValid(email)) {
                    email_signup.setError("A valid email is required!");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    pass_signup.setError("Password is required!");
                    return;
                } else if (pass.length() < 6) {
                    pass_signup.setError("Password must be at least 6 characters");
                    return;
                }
                signup_dialog.setMessage("Processing...");
                signup_auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            signup_dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Signup complete",Toast.LENGTH_SHORT).show();
                            String username = name_signup.getText().toString().trim();

                            String userId = signup_auth.getCurrentUser().getUid();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(userId).child("username").setValue(username);

                            startActivity(new Intent(getApplicationContext(),Onboarding_login_Activity.class));
                        }else{
                            signup_dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Signup fail!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        have_acc.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Onboarding_login_Activity.class)));
        btn_back_signup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),onboarding_2_Activity.class)));

    }
    private boolean isEmailValid(String email) {
        // Đây là một kiểm tra đơn giản cho email hợp lệ. Bạn có thể sử dụng biểu thức chính quy mạnh hơn.
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}