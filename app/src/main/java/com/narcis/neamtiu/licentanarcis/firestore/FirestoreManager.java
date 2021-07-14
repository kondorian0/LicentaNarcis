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
import com.narcis.neamtiu.licentanarcis.activities.DayEventsActivity;
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

    private FirestoreManager() { }

    public static FirestoreManager getInstance() {
        return singleton;
    }

    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private ArrayList<EventData> eventDataArrayList = new ArrayList();

    public void registerUser(final RegisterUserActivity activity, User userInfo) {
        //Daca colectia este deja creata atunci nu se va crea din nou
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
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
        //numele colectiei a carei data avem nevoie
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
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
        String newEventId = mFireStore.collection(Constants.EVENTS)
                .document().getId();
        eventData.setEventId(newEventId);

        mFireStore.collection(Constants.EVENTS)
                .document(newEventId)
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
                            eventData.eventId = (String) doc.getId();
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

    public void deleteEvent(final DayEventsActivity activity, final String eventId) {
        mFireStore.collection(Constants.EVENTS)
                .document(eventId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.eventDeleteSuccess(eventId);
                        for (EventData eventData : eventDataArrayList) {
                            if (eventData.getEventId() == eventId) {
                                eventDataArrayList.remove(eventData);
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ERROR", "Error while deleting the event",e);
                    }
                });
    }

    public void uploadFileToCloudStorage(String filename, Uri fileURI, final OnImageUploadListener listener) {
        StorageReference sRef = FirebaseStorage.getInstance()
                .getReference()
                .child(filename);

        sRef.putFile(fileURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("Firebase file URL", Objects.requireNonNull(taskSnapshot.getMetadata()).getReference().getDownloadUrl().toString());

                //preia url-ul downloadat din taskSnapshot
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
                Log.e("Failed", e.getMessage());
            }
        });
    }

    public ArrayList<EventData> getEventsListFromFirestore() {
        return this.eventDataArrayList;
    }

    public String getCurrentUserID() {
        //preia userul current folosind FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserID = "";
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }
        return currentUserID;
    }
}
