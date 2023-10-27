package com.example.orixpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.auth.SignInMethodQueryResult;

public class Onboarding_login_Activity extends AppCompatActivity {

    private EditText email_login;
    private EditText pass_login;
    private TextView btn_back_login;
    private TextView forgot_pass;
    private Button btnlogin;
    private TextView dont_have_acc;
    private FirebaseAuth login_auth;
    private TextView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_login);
        login_auth = FirebaseAuth.getInstance();
        login();
    }

    private void login() {
        email_login = findViewById(R.id.email_login);
        pass_login = findViewById(R.id.password_login);
        forgot_pass = findViewById(R.id.forgot_pass);
        btnlogin = findViewById(R.id.btnlogin);
        dont_have_acc = findViewById(R.id.dont_have_acc);
        btn_back_login = findViewById(R.id.btn_back_login);


        btn_back = findViewById(R.id.btn_back_login);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_login.getText().toString().trim();
                String pass = pass_login.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_login.setError("Email is required!");
                    return;
                } else if (!isEmailValid(email)) {
                    email_login.setError("A valid email is required!");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    pass_login.setError("Password is required!");
                    return;
                } else if (pass.length() < 6) {
                    pass_login.setError("Password must be at least 6 characters");
                    return;
                }

                // Kiểm tra xem email đã được sử dụng để đăng ký tài khoản chưa
                login_auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result.getSignInMethods() == null || result.getSignInMethods().isEmpty()) {
                                // Email chưa được sử dụng để đăng ký tài khoản
                                showSignUpDialog();
                            } else {
                                // Email đã được sử dụng để đăng ký tài khoản
                                login_auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), nav.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login fail: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forgot_pass.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPass.class)));
        dont_have_acc.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Onboarding_signup_Activity.class)));
        btn_back_login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), onboarding_2_Activity.class)));
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showSignUpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Account Not Found")
                .setMessage("There is no account associated with this email address. Do you want to sign up?")
                .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Onboarding_signup_Activity.class));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
