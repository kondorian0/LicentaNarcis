package com.narcis.neamtiu.licentanarcis.firestore;

import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.narcis.neamtiu.licentanarcis.activities.LoginUserActivity;
import com.narcis.neamtiu.licentanarcis.activities.RegisterUserActivity;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.models.User;
import com.narcis.neamtiu.licentanarcis.util.Constants;

import java.util.ArrayList;
import java.util.Objects;

public class FirestoreManager {
    // Observer
    public interface Observer {
        void onDataEventRegistered(EventData eventData);
    }

    public interface OnImageUploadListener {
        void onFileUpload(String imageUrl);
    }

    ArrayList<Observer> mObservers = new ArrayList<>();

    public void register(Observer observer) {
        mObservers.add(observer);
    }

    public void unregister(Observer observer) {
        mObservers.remove(observer);
    }

    private void notifyObserversEventRegistered(EventData eventData) {
        for (Observer l : mObservers) {
            l.onDataEventRegistered(eventData);
        }
    }

    // Singleton
    private static FirestoreManager singleton = new FirestoreManager();

    private FirestoreManager() {
    }

    public static FirestoreManager getInstance() {
        return singleton;
    }

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private ArrayList<EventData> eventDataArrayList = new ArrayList();

    public void registerUser(final RegisterUserActivity activity, User userInfo) {
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

    public void getUserDetails(final LoginUserActivity activity) {
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

    public void registerDataEvent(final EventData eventData) {
        mFireStore.collection(Constants.EVENTS)
                .document()
                .set(eventData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SUCCESS", "Data added successfully");
                        notifyObserversEventRegistered(eventData);
                        eventDataArrayList.add(eventData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", "Error while registering the user", e);
                    }
                });
    }

    public void getUserEventsList() {
        mFireStore.collection(Constants.EVENTS)
                .whereEqualTo("userId", getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.e("List", queryDocumentSnapshots.toString());
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            EventData eventData = doc.toObject(new EventData().getClass());
                            eventData.eventType = (String) doc.get("eventType");
                            eventData.eventTitle = (String) doc.get("eventTitle");
                            eventData.eventDate = (String) doc.get("eventDate");
                            eventData.eventContent = (String) doc.get("eventContent");
                            eventData.eventDescription = (String) doc.get("eventDescription");
                            eventDataArrayList.add(eventData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public ArrayList<EventData> getEventsListFromFirestore() {
        return this.eventDataArrayList;
    }

    public String getCurrentUserID() {
        //get currentUser using FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserID = "";
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }
        return currentUserID;
    }

    public void uploadFileToCloudStorage(String filename, Uri fileURI, final OnImageUploadListener listener) {
        StorageReference sRef = FirebaseStorage.getInstance()
                .getReference()
                .child(filename);

        sRef.putFile(fileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("Firebase file URL", Objects.requireNonNull(taskSnapshot.getMetadata()).getReference().getDownloadUrl().toString());

                //get  the downloadable url from the task snapshot
                Objects.requireNonNull(taskSnapshot.getMetadata().getReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("Downloadable FILE URL", uri.toString());
                        listener.onFileUpload(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failed",e.getMessage());
            }
        });
    }
}
