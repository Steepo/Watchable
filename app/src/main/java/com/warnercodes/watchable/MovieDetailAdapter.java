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
    Context context;
    private List<Movie> movies;

    public MovieDetailAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public void add(int position, Movie item) {
        movies.add(position, item);
        notifyItemInserted(position);
    }
    public void remove(Movie item) {
        int position = movies.indexOf(item);
        movies.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent,false);
        CoverSimilarViewHolder viewHolder = new CoverSimilarViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CoverSimilarViewHolder viewHolder = (CoverSimilarViewHolder) holder;

        viewHolder.main_title.setText(movies.get(position).getTitle());
        viewHolder.trama.setText(movies.get(position).getOverview());
        Glide.with(((CoverSimilarViewHolder) holder).main_cover.getContext()).load(movies.get(position).getCopertina()).into(viewHolder.main_cover);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private static class CoverSimilarViewHolder extends RecyclerView.ViewHolder {
        private ImageView main_cover;
        private TextView main_title;
        private TextView trama;
        CoverSimilarViewHolder(View view) {
            super(view);
            this.main_cover = view.findViewById(R.id.main_cover);
            this.main_title = view.findViewById(R.id.main_title);
            this.trama = view.findViewById(R.id.trama);
        }
    }
}
