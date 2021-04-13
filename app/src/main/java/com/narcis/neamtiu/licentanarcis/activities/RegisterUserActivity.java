package com.narcis.neamtiu.licentanarcis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.User;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText mFullName, mEmail, mPassword;
    private AppCompatButton mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        mFullName = findViewById(R.id.full_name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.registerButton);

        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    private void registerUser() {
        final String name = mFullName.getText().toString();
        final String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        if(name.isEmpty()) {
            mFullName.setError("Full name is required");
            mFullName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide valid email");
            mEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            mPassword.setError("Age is required");
            mPassword.requestFocus();
            return;
        }

        //Firebase does not accept password less than 5 characters
        if(password.length() < 6) {
            mPassword.setError("Min password length should contain at lest 6 characters");
            mPassword.requestFocus();
            return;
        }

        //create an instance and create a register user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Registered user
                            User user = new User(name, email);

                            FirestoreClass.getInstance().registerUser(RegisterUserActivity.this, user);

                        } else {
                            Toast.makeText(RegisterUserActivity.this,
                                    "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void userRegistrationSuccess() {
        Toast.makeText(RegisterUserActivity.this,
                "User has been registered succesfully!", Toast.LENGTH_SHORT).show();

        FirebaseAuth.getInstance().signOut();
        finish();
    }
}