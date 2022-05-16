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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatButton btnRegister;
    private AppCompatEditText etName,etAge,etEmailRegister,etPasswordRegister;
    private ProgressBar progressBarRegister;
    private Toolbar toolbar_register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        btnRegister=(AppCompatButton) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        toolbar_register=(Toolbar)findViewById(R.id.toolbar_register);
        toolbar_register.setOnClickListener(this);

        etName=(AppCompatEditText) findViewById(R.id.etName);
        etAge=(AppCompatEditText) findViewById(R.id.etAge);
        etEmailRegister=(AppCompatEditText) findViewById(R.id.etEmailRegister);
        etPasswordRegister=(AppCompatEditText) findViewById(R.id.etPasswordRegister);

        progressBarRegister=(ProgressBar) findViewById(R.id.progressBarRegister);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.toolbar_register:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }


    private void registerUser(){
        String email=etEmailRegister.getText().toString().trim();
        String password=etPasswordRegister.getText().toString().trim();
        String fullName=etName.getText().toString().trim();
        String age=etAge.getText().toString().trim();

        if(fullName.isEmpty()){
            etName.setError("Full name is required!");
            etName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            etAge.setError("age is required!");
            etAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmailRegister.setError("Email is required!");
            etEmailRegister.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailRegister.setError("Please provide valid email");
            etEmailRegister.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPasswordRegister.setError("Password is required!");
            etPasswordRegister.requestFocus();
            return;
        }
        if(password.length()<6){
            etPasswordRegister.setError("password should be at least 6 characters long!!");
            etPasswordRegister.requestFocus();
            return;
        }

        progressBarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(fullName, age, email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                    progressBarRegister.setVisibility(View.GONE);
                                    //

                                }else{
                                    Toast.makeText(RegisterActivity.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                                    progressBarRegister.setVisibility(View.GONE);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"Failed to register try again!",Toast.LENGTH_LONG).show();
                        progressBarRegister.setVisibility(View.GONE);
                    }
                }
            });

    }

}