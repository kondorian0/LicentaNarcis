package com.narcis.neamtiu.licentanarcis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.narcis.neamtiu.licentanarcis.R;
import com.narcis.neamtiu.licentanarcis.util.EventListData;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private EventListData[] listData;

    public EventListAdapter(EventListData[] listData) {
        this.listData = listData;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    @NonNull
    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final EventListData myListData = listData[position];
        holder.textViewTitle.setText(listData[position].getTitle());
        holder.textViewDetails.setText(listData[position].getDescription());
        holder.textViewDate.setText(listData[position].getDate());
        holder.imageView.setImageResource(listData[position].getImgId());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"title: "+myListData.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listData.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle, textViewDetails, textViewDate;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.listItemImage);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.titleEvent);
            this.textViewDetails = (TextView) itemView.findViewById(R.id.detailsEvent);
            this.textViewDate = (TextView) itemView.findViewById(R.id.dateEvent);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutItem);
        }
    }
}
