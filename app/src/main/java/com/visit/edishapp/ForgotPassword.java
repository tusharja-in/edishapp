package com.visit.edishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private AppCompatEditText etEmailForgot;
    private AppCompatButton btnResetPassword;
    private ProgressBar progressBarForgot;
    private Toolbar toolbar_forgot;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        toolbar_forgot=(Toolbar) findViewById(R.id.toolbar_forgot);
        etEmailForgot=(AppCompatEditText) findViewById(R.id.etEmailForgot);
        progressBarForgot=(ProgressBar) findViewById(R.id.progressBarForgot);
        btnResetPassword=(AppCompatButton) findViewById(R.id.btnResetPassword);
         mAuth=FirebaseAuth.getInstance();

         toolbar_forgot.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(ForgotPassword.this, MainActivity.class));
             }
         });



         btnResetPassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 resetPassword();
             }
         });

    }

    private void resetPassword() {
        String email=etEmailForgot.getText().toString().trim();

        if(email.isEmpty()){
            etEmailForgot.setError("Email is required!");
            etEmailForgot.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailForgot.setError("Please provide valid email");
            etEmailForgot.requestFocus();
            return;
        }

        progressBarForgot.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your mail!",Toast.LENGTH_LONG).show();
                    progressBarForgot.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgotPassword.this,"Try again!!",Toast.LENGTH_LONG).show();
                    progressBarForgot.setVisibility(View.GONE);
                }
            }
        });
    }
}