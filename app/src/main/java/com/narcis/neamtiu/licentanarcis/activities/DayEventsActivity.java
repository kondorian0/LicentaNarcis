package com.narcis.neamtiu.licentanarcis.activities;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.adapters.EventListAdapter;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;
import com.narcis.neamtiu.licentanarcis.util.DownloadImageTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class DayEventsActivity extends AppCompatActivity {

    private FirestoreManager firestoreManager = FirestoreManager.getInstance();
    private ArrayList<EventData> allDataList = firestoreManager.getEventsListFromFirestore();
    private ArrayList<EventData> dayDataList = new ArrayList<EventData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_events_list);

        TextView noEventsTextView = findViewById(R.id.noEventsTextView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        AppCompatImageButton deleteEventButton = findViewById(R.id.deleteEventButton);

        for (int i = 0; i < allDataList.size(); i++) {
            String eventDate = allDataList.get(i).getEventDate();
            if (eventDate.equals(dateSelected())) {
                dayDataList.add(allDataList.get(i));
                noEventsTextView.setVisibility(View.GONE);
            }
        }

        EventListAdapter adapter = new EventListAdapter(dayDataList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private String dateSelected() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String value = extras.getString(Constants.SELECTED_DATE);
            return value;
        } else {
            return null;
        }
    }

    public void showNoteDialog(String eventContent) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_event_note);

        //Initializing the views of the dialog.
        final TextView textContent = dialog.findViewById(R.id.textContentView);

        textContent.setText(eventContent);

        dialog.show();
    }

    public void showEventLocationDialog(String title, String description, String location) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_event_location);

        //Initializing the views of the dialog.
        final TextView titleEvent = dialog.findViewById(R.id.titleEventDialog);
        final TextView descriptionEvent = dialog.findViewById(R.id.descriptionEventDialog);
        final TextView locationEvent = dialog.findViewById(R.id.locationEventDialog);

        titleEvent.setText(title);
        descriptionEvent.setText(description);
        locationEvent.setText(location);

        dialog.show();
    }

    public void showImageDialog(String eventContent) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_event_image);

        //Initializing the views of the dialog.
        ImageView imageContentView = dialog.findViewById(R.id.imageContentView);

        new DownloadImageTask(imageContentView).execute(eventContent);

//        Picasso.get()
//                .load(eventContent)
//                .error(R.drawable.ic_note_color)
//                .into(imageContentView);

        dialog.show();

    }

    public void showAudioDialog(final String filename, final String eventContent) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        final MediaPlayer mediaPlayer = new MediaPlayer();

        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_event_audio);

        //Initializing the views of the dialog.
        final TextView textContent = dialog.findViewById(R.id.textAudioContentView);
        final Button playAudio = dialog.findViewById(R.id.playAudioDialogButton);
        final Button stopAudio = dialog.findViewById(R.id.stopAudioDialogButton);

        stopAudio.setVisibility(View.GONE);

        textContent.setText(filename);

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                playAudio.setVisibility(View.GONE);
                stopAudio.setVisibility(View.VISIBLE);

                try {
                    mediaPlayer.setDataSource(eventContent);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                stopAudio.setVisibility(View.GONE);
                playAudio.setVisibility(View.VISIBLE);

                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });

        dialog.show();
    }

    public void deleteEvent(String eventID){
        Toast.makeText(this, "You can now delete the event "+eventID, Toast.LENGTH_SHORT).show();
    }

    public void deleteEventSucces(){
        firestoreManager.getEventsListFromFirestore();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DayEventsActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
