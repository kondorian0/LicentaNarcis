package com.narcis.neamtiu.licentanarcis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<EventTest>
{
    private static final String TAG = "EventListAdapter";
    private Context mContext;
    private int mResource;

    public EventListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<EventTest> objects, Context mContext)
    {
        super(context, resource, objects);
        this.mContext = mContext;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        // get event information
        String title = getItem(position).getTitle();
        String details = getItem(position).getDetails();
        String date = getItem(position).getDate();

        // create event object with the information
        EventTest eventTest = new EventTest(title,details,date);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.titleEvent);
        TextView tvDetails = (TextView) convertView.findViewById(R.id.detailsEvent);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateEvent);

        tvTitle.setText(title);
        tvDetails.setText(details);
        tvDate.setText(date);

        return convertView;
    }
}
