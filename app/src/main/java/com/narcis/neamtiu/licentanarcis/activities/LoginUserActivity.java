package com.narcis.neamtiu.licentanarcis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.narcis.neamtiu.licentanarcis.R;

public class LoginUserActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mRegister;
    private EditText mEmail, mPassword;
    private AppCompatButton mSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mRegister = findViewById(R.id.register);
        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mSignIn = findViewById(R.id.signInButton);

        mRegister.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;

            case R.id.signInButton:
                userlogin();
                break;
        }
    }

    private void userlogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (email.isEmpty()){
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPassword.setError("Age is required");
            mPassword.requestFocus();
            return;
        }

        //Firebase does not accept password less than 5 characters
        if(password.length() < 6){
            mPassword.setError("Min password length should contain at lest 6 characters");
            mPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginUserActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();

                    //redirect to calendar
                    startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                }else {
                    Toast.makeText(LoginUserActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}