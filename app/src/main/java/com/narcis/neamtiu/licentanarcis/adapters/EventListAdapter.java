package com.narcis.neamtiu.licentanarcis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    ArrayList<String> list;
    Context context;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param list ArrayList<String> containing the data to populate views to be used
     * by RecyclerView.
     */
    public EventListAdapter(Context contxt, ArrayList<String> itemsList) {
        list = itemsList;
        context = contxt;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    @NonNull
    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String model = list.get(position);
        holder.itemView.findViewById(R.id.titleEvent) = model.title;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, detailsView, dateView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.titleEvent);
            detailsView = view.findViewById(R.id.detailsEvent);
            dateView = view.findViewById(R.id.dateEvent);
            imageView = view.findViewById(R.id.list_image);
            // Define click listener for the ViewHolder's View
        }
    }
}
