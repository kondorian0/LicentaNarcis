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
import com.google.firebase.auth.FirebaseUser;
import com.google.protobuf.Any;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreClass;
import com.narcis.neamtiu.licentanarcis.models.User;
import com.narcis.neamtiu.licentanarcis.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText mFullName, mEmail, mPassword;
    private AppCompatButton mRegisterBtn;
    private ArrayList<HashMap<String, Any>> events;

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
        events = new ArrayList<HashMap<String, Any>>();

//        HashMap<String, String> something = new HashMap<>();
//        something.put(Constants.TYPE_OF_EVENT, "Somehting to see");
//
//        events.add(something);


        if(name.isEmpty()){
            mFullName.setError("Full name is required");
            mFullName.requestFocus();
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

        //create an instance and create a register user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Registered user
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User(name, email, events);

                            new FirestoreClass().registerUser(RegisterUserActivity.this, user);

                        }else {
                            Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                        }
//                            User user = new User(name, email);

//                            FirebaseDatabase.getInstance().getReference("Users")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(RegisterUserActivity.this, "User has been registered succesfully!", Toast.LENGTH_SHORT).show();
//
//                                        FirebaseAuth.getInstance().signOut();
//                                        finish();
//                                    }else {
//                                        Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }else {
//                            Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
    }
    public void userRegistrationSuccess() {
        Toast.makeText(RegisterUserActivity.this, "User has been registered succesfully!", Toast.LENGTH_SHORT).show();

        FirebaseAuth.getInstance().signOut();
        finish();
    }
}