package com.narcis.neamtiu.licentanarcis.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.google.firebase.database.FirebaseDatabase;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.User;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView mRegisterTitle;
    private EditText mFullName, mAge, mEmail, mPassword;
    private AppCompatButton mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        mRegisterTitle = findViewById(R.id.register_title);
        mFullName = findViewById(R.id.full_name);
        mAge = findViewById(R.id.age);
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
        final String fullName = mFullName.getText().toString();
        final String age = mAge.getText().toString();
        final String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();


        if(fullName.isEmpty()){
            mFullName.setError("Full name is required");
            mFullName.requestFocus();
            return;
        }

        if(age.isEmpty()){
            mAge.setError("Age is required");
            mAge.requestFocus();
            return;
        }else if(!(age.matches("\\d+(?:\\.\\d+)?"))){
            mAge.setError("Enter age in number format");
            mAge.requestFocus();
            return;
        }

        if(email.isEmpty()){
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please provide valid email");
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

        mAuth.createUserWithEmailAndPassword(email, password)
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
                                        Toast.makeText(RegisterUserActivity.this, "User has been registered succesfully!", Toast.LENGTH_SHORT).show();

                                        //Redirect to login layout
                                    }else {
                                        Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}