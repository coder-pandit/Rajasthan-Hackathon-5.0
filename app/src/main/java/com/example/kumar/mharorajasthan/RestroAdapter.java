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
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestroAdapter extends RecyclerView.Adapter<RestroAdapter.RestroViewHolder> {

    private List<Place> restros;
    private Context mContext;

    public RestroAdapter(Context context, List<Place> restros) {
        this.restros = restros;
        mContext = context;
    }

    @NonNull
    @Override
    public RestroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_item, parent, false);

        return new RestroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestroViewHolder holder, int position) {
        final int i = position;

        holder.restroName.setText(restros.get(i).getName());
        holder.restroRating.setRating(restros.get(i).getRating() / 2);

        Picasso.get()
                .load(restros.get(i).getThumbnailUrl())
                .into(holder.restroImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Place place = restros.get(i);
                Uri uri = Uri.parse(place.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restros.size();
    }

    class RestroViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView restroImage;
        TextView restroName;
        RatingBar restroRating;

        public RestroViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            restroImage = mView.findViewById(R.id.package_image);
            restroName = mView.findViewById(R.id.package_name);
            restroRating = mView.findViewById(R.id.package_rating);
        }
    }
}
