package com.example.kumar.mharorajasthan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Place> places;
    private Context mContext;

    HomeAdapter(Context context, List<Place> places) {
        this.places = places;
        mContext = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_place_item, parent, false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final int i = position;

        holder.placeNameView.setText(places.get(i).getName());

        Picasso.get()
                .load(places.get(i).getThumbnailUrl())
                .into(holder.placeImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place place = places.get(i);
                Uri uri = Uri.parse(place.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView placeImageView;
        TextView placeNameView;

        HomeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            placeImageView = mView.findViewById(R.id.place_image);
            placeNameView = mView.findViewById(R.id.place_name);
        }


    }
}
