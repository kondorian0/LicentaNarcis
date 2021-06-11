package com.narcis.neamtiu.licentanarcis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.activities.DayEventsActivity;
import com.narcis.neamtiu.licentanarcis.models.EventData;
import com.narcis.neamtiu.licentanarcis.util.Constants;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private ArrayList<EventData> listData ;
    private DayEventsActivity activity;

    public EventListAdapter(ArrayList<EventData> listData, DayEventsActivity activity) {
        this.listData = listData;
        this.activity = activity;
    }

    @NonNull
    // Creaza noi views
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    // Inlocuieste continutul unei view
    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        // Obtine elementul din dataset aflat in pozitia currenta si inlocuieste continutul view-ului cu acel element.
        final EventData model = listData.get(position);
        holder.textViewTitle.setText(listData.get(position).getEventTitle());
        holder.textViewTime.setText(listData.get(position).getEventTime());
        holder.imageView.setImageResource(listData.get(position).getEventIcon());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (model.getEventType()){
                    case Constants.LOCATION_EVENT:
                        activity.showEventLocationDialog(
                                model.getEventTitle(),
                                model.getEventDescription(),
                                model.getEventLocation()
                        );
                        break;
                    case Constants.NOTE_EVENT:
                        activity.showNoteDialog(model.getEventContent());
                        break;
                    case Constants.DRAW_EVENT:
                        activity.showImageDialog(model.getEventContent());
                        break;
                    case Constants.RECORD_EVENT:
                        activity.showAudioDialog(model.getEventTitle(), model.getEventContent());
                }
            }
        });
        holder.deleteEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO delete element
                activity.deleteEvent(model.getEventId());
            }
        });
    }

    // Returneaza marimea dataset-ului
    @Override
    public int getItemCount() {
        return listData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle, textViewTime;
        public AppCompatImageButton deleteEventView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.listItemImage);
            this.textViewTitle = itemView.findViewById(R.id.titleEvent);
            this.textViewTime = itemView.findViewById(R.id.timeEvent);
            this.deleteEventView = itemView.findViewById(R.id.deleteEventButton);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutItem);
        }
    }
}
