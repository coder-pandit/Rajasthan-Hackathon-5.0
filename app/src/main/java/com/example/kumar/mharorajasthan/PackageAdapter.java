package com.example.kumar.mharorajasthan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private List<Tour> packages;
    private Context mContext;

    public PackageAdapter(Context context, List<Tour> packages) {
        this.packages = packages;
        mContext = context;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_item, parent, false);

        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        final int i = position;

        holder.restroName.setText(packages.get(i).getTitle());
        holder.restroRating.setRating((float) (packages.get(i).getRating() / 2));
        holder.currentPrice.setText(String.valueOf(packages.get(i).getPrice()));
        holder.actualPrice.setText(String.valueOf(packages.get(i).getOriginalPrice()));

        Picasso.get()
                .load(packages.get(i).getPhotoUrl())
                .into(holder.restroImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tour place = packages.get(i);
                Uri uri = Uri.parse(place.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView restroImage;
        TextView restroName;
        TextView currentPrice;
        TextView actualPrice;
        RatingBar restroRating;

        PackageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            restroImage = mView.findViewById(R.id.package_image);
            restroName = mView.findViewById(R.id.package_name);
            currentPrice = mView.findViewById(R.id.package_current_price);
            actualPrice = mView.findViewById(R.id.package_actual_price);
            restroRating = mView.findViewById(R.id.package_rating);
        }
    }
}
