package com.warnercodes.watchable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.warnercodes.watchable.ui.activity.Movie;

import java.util.List;

class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> movies;

    public MovieDetailAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    private Context context;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_activities, parent,false);
        return new RecentActivitiesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecentActivitiesHolder viewHolder = (RecentActivitiesHolder) holder;

        Glide.with(context).load("url").into(viewHolder.main_cover);
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private static class RecentActivitiesHolder extends RecyclerView.ViewHolder {
        private ImageView main_cover;
        RecentActivitiesHolder(View view) {
            super(view);
            this.main_cover = view.findViewById(R.id.main_cover);
        }
    }
}
