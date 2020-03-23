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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover_similar, parent,false);
        return new CoverSimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CoverSimilarViewHolder viewHolder = (CoverSimilarViewHolder) holder;

        viewHolder.main_title.setText(movies.get(position).getTitle());
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

    private static class CoverSimilarViewHolder extends RecyclerView.ViewHolder {
        private ImageView main_cover;
        private TextView main_title;
        CoverSimilarViewHolder(View view) {
            super(view);
            this.main_cover = view.findViewById(R.id.main_cover);
            this.main_title = view.findViewById(R.id.main_title);
        }
    }
}
