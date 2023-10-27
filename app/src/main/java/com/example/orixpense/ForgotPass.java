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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPass extends AppCompatActivity {

    private EditText fEmail;
    private Button Fcontinue;
    private FirebaseAuth F_pass;
    private TextView btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        F_pass = FirebaseAuth.getInstance();
        fogotpass();
    }

    protected void fogotpass(){
        fEmail = findViewById(R.id.sent_email);
        Fcontinue = findViewById(R.id.btn_continue_forgotP);
        btn_back = findViewById(R.id.btn_back_fogot);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Fcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = fEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    fEmail.setError("Email is required!");
                    return;
                }else if (!isEmailValid(email)) {
                    fEmail.setError("A valid email is required!");
                    return;
                }

                Fcontinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = fEmail.getText().toString().trim();
                        if (TextUtils.isEmpty(email)){
                            fEmail.setError("Email is required!");
                            return;
                        }

                        // Kiểm tra xem email đã được sử dụng để đăng ký tài khoản chưa
                        F_pass.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    if (result.getSignInMethods() == null || result.getSignInMethods().isEmpty()) {
                                        // Email chưa được sử dụng để đăng ký tài khoản
                                        showSignUpDialog();
                                    } else {
                                        // Email đã được sử dụng để đăng ký tài khoản
                                        F_pass.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), forgot_pass_emailsent.class));
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Email sent fail!", Toast.LENGTH_SHORT).show();
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

            }
        });

    }

    private boolean isEmailValid(String email) {
        // Đây là một kiểm tra đơn giản cho email hợp lệ. Bạn có thể sử dụng biểu thức chính quy mạnh hơn.
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showSignUpDialog() {
        // Hiển thị thông báo và hỏi người dùng có muốn đăng ký tài khoản không
        new AlertDialog.Builder(this)
                .setTitle("Account Not Found")
                .setMessage("There is no account associated with this email address. Do you want to sign up?")
                .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Chuyển sang màn hình đăng ký tài khoản
                        startActivity(new Intent(getApplicationContext(), Onboarding_signup_Activity.class));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}