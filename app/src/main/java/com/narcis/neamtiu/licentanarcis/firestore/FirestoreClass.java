package com.narcis.neamtiu.licentanarcis.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.narcis.neamtiu.licentanarcis.activities.LoginUserActivity;
import com.narcis.neamtiu.licentanarcis.activities.MainActivity;
import com.narcis.neamtiu.licentanarcis.activities.NoteActivity;
import com.narcis.neamtiu.licentanarcis.activities.RegisterUserActivity;
import com.narcis.neamtiu.licentanarcis.models.EventContent;
import com.narcis.neamtiu.licentanarcis.models.User;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;

public class FirestoreClass {

    private FirebaseFirestore  mFireStore = FirebaseFirestore.getInstance();

    public void registerUser(final RegisterUserActivity activity, User userInfo){

        //if the collection is already created then it will not create the same or another
        mFireStore.collection(Constants.USERS)
                //Id for users fields
                .document(getCurrentUserID())
                //userInfo are Field and the SetOption is set to merge instead of replace fields.
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.userRegistrationSuccess();  //Toast with succes
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "Error while registering the user", e);
                    }
                });

    }

    public void setEventContent(final DialogDateTimeHelper activity, EventContent eventContent){

        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .set(eventContent, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.sendNoteData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "Error while registering the user", e);
                    }
                });
    }

    public void updateUserProfileData(final DialogDateTimeHelper activity, EventContent eventContent){

        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .set(eventContent, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.sendNoteData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "Error while registering the user", e);
                    }
                });
    }

    public String getCurrentUserID(){
        //get currentUser using FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserID = "";
        if(currentUser != null){
            currentUserID = currentUser.getUid();
        }

        return currentUserID;
    }

    public void getUserDetails(final LoginUserActivity activity){
        //collection name from which we want the data
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Log.i("TAG", documentSnapshot.toString());
//                        //convert snapshot doc to User Data model object
//                        User user = documentSnapshot.toObject(User.class);
//
//                        SharedPreferences sharedPreferences =
//                                activity.getSharedPreferences(
//                                        Constants.EVENTS,
//                                        Context.MODE_PRIVATE);
//
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        // KEY: constants.events  ----  Value: user.name
//                        editor.putString(Constants.TYPE_OF_EVENT, user.name);
//                        editor.apply();

                        //TODO pass the result to the login actity
                        //start
                        activity.userLoggedInSucces();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
