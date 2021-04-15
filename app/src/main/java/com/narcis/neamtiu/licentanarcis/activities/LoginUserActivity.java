package com.narcis.neamtiu.licentanarcis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;

public class LoginUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mRegister;
    private EditText mEmail, mPassword;
    private AppCompatButton mSignIn;

    private FirebaseAuth mAuth;
    private FirestoreManager firestoreManager = FirestoreManager.getInstance();


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
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please enter a valid email");
            mEmail.requestFocus();
            return;
        }

        //Firebase does not accept password less than 5 characters
        if(password.length() < 6){
            mPassword.setError("Min password length should contain at lest 6 characters");
            mPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginUserActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    //redirect to calendar
                    firestoreManager.getUserDetails(LoginUserActivity.this);
                    firestoreManager.getUserEventsList();
//                    startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
//                    finish();
                } else {
                    Toast.makeText(LoginUserActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void userLoggedInSucces(){
        Intent startIntent = new Intent(LoginUserActivity.this, MainActivity.class);
//        startIntent.putExtra("firestoreClass", firestoreClass);
        startActivity(startIntent);
        finish();
    }
}