package com.narcis.neamtiu.licentanarcis.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.activities.LoginUserActivity;
import com.narcis.neamtiu.licentanarcis.activities.RegisterUserActivity;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.models.User;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DialogDateTimeHelper;
import com.narcis.neamtiu.licentanarcis.util.EventListData;

import java.util.ArrayList;

public class FirestoreClass {

    private FirebaseFirestore  mFireStore = FirebaseFirestore.getInstance();
    private ArrayList<EventListData> listData;

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

    public void registerDataEvent(final DialogDateTimeHelper activity, EventData eventData){

        mFireStore.collection(Constants.EVENTS)
                .document()
                .set(eventData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SUCCESS", "Data added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "Error while registering the user", e);
                    }
                });
    }

    public void getUserData(){

        mFireStore.collection(Constants.EVENTS)
                .whereEqualTo("userId", getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("List", queryDocumentSnapshots.toString());
                        ArrayList<EventData> eventDataArrayList = new ArrayList();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){

                            EventData eventData = doc.toObject(new EventData().getClass());
                            eventData.eventType = (String) doc.get("eventType");
                            eventData.eventTitle = (String) doc.get("eventTitle");
                            eventData.eventDate = (String) doc.get("eventDate");
                            eventData.eventContent = (String) doc.get("eventContent");
                            eventData.eventDescription = (String) doc.get("eventDescription");

                            eventDataArrayList.add(eventData);
                        }

                        buildEventsList(eventDataArrayList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void buildEventsList(ArrayList<EventData> eventsList){

        listData = new ArrayList<EventListData>();

        for(EventData i : eventsList){
            int eventImageIcon;
            String eventName, eventDetails, eventDate;
            
            switch (i.eventType){
                case "Note":
                    eventImageIcon = R.drawable.ic_note_color;
                    eventName = i.eventContent;
                    eventDetails = "";
                    break;
                case "Location Event":
                    eventImageIcon = R.drawable.ic_location_color;
                    eventName = i.eventTitle;
                    eventDetails = i.eventDescription;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i.eventType);
            }

//            if(i.eventTitle == null){
//                eventName = i.eventContent;
//            }else {
//                eventName = i.eventTitle;
//            }
//
//            if(i.eventContent == null){
//                eventDetails = i.eventDescription;
//            }else {
//                eventDetails = i.eventContent;
//            }
            
            eventDate = i.eventDate;

            listData.add(new EventListData(eventName, eventDetails, eventDate, eventImageIcon));
        }
    }

    public ArrayList<EventListData> getEventsListFromFirestore() {
        return this.listData;
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
}
