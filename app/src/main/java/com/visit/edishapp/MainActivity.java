package com.visit.edishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register,tvForgot;

    private AppCompatButton btnLogin;
    private AppCompatEditText etPassword,etEmail;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.tvRegister);
        register.setOnClickListener(this);
        tvForgot=(TextView) findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(this);
        btnLogin=(AppCompatButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        etEmail=(AppCompatEditText) findViewById(R.id.etEmail);
        etPassword=(AppCompatEditText) findViewById(R.id.etPassword);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.tvForgot:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email= etEmail.getText().toString().trim();
        String password= etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide valid email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("password should be at least 6 characters long!!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "Success login!", Toast.LENGTH_LONG).show();
                        //
                        startActivity(new Intent(MainActivity.this,DocumentView.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"check your mail",Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(MainActivity.this,"Failed to login!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}