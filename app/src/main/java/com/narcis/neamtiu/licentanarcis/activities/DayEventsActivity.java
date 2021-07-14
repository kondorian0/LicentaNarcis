package com.narcis.neamtiu.licentanarcis.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_event_note);

        final TextView textContent = dialog.findViewById(R.id.textContentView);

        textContent.setText(eventContent);

        dialog.show();
    }

    public void showEventLocationDialog(String title, String description, String location) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_event_location);

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

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_event_image);

        ImageView imageContentView = dialog.findViewById(R.id.imageContentView);

        new DownloadImageTask(imageContentView).execute(eventContent);
        dialog.show();
    }

    public void showAudioDialog(final String filename, final String eventContent) {
        final Dialog dialog = new Dialog(DayEventsActivity.this);

        final MediaPlayer mediaPlayer = new MediaPlayer();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_event_audio);

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

    public void deleteEvent(String eventID) {
        showAlertDialogToDeleteEvent(eventID);
    }

    public void eventDeleteSuccess(String eventId) {
        Toast.makeText(this, "Event deleted successfully ", Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        for (EventData eventData : dayDataList) {
            if (eventData.getEventId() == eventId) {
                dayDataList.remove(eventData);
                break;
            }
        }
        EventListAdapter adapter = new EventListAdapter(dayDataList, this);
        recyclerView.setAdapter(adapter);
    }

    private void showAlertDialogToDeleteEvent(final String eventId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete the event?");
        builder.setIcon(R.drawable.ic_warning_dialog);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firestoreManager.deleteEvent(DayEventsActivity.this, eventId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DayEventsActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
