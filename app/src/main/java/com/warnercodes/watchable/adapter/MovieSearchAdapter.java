package com.warnercodes.watchable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.MovieDetailActivity;
import com.warnercodes.watchable.databinding.ItemMovieSearchBinding;

import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Movie> movies;

    public MovieSearchAdapter(List<Movie> movies) {
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
        ItemMovieSearchBinding view = ItemMovieSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        context = viewHolder.search_thumb.getContext();
        // Add Similar movies
        ImageView search_thumb = viewHolder.search_thumb;
        TextView textView = viewHolder.search_movie_title;
        textView.setText(movies.get(position).getTitle());
        viewHolder.search_movie_year.setText(movies.get(position).getReleaseDate().split("-")[0]); //shows only the year
        Glide.with(context).load(movies.get(position).getCopertina()).into(viewHolder.search_thumb);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView search_thumb;
        private TextView search_movie_title;
        private ConstraintLayout clickable;
        private TextView search_movie_year;

        ViewHolder(ItemMovieSearchBinding binding) {
            super(binding.getRoot());
            search_thumb = binding.searchThumb;
            search_movie_title = binding.searchMovieTitle;
            clickable = binding.clickable;
            search_movie_year = binding.searchMovieYear;
            clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = movies.get(getAdapterPosition()).getMovieId();
                    String title = movies.get(getAdapterPosition()).getTitle();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movieId", id);
                    intent.putExtra("title", title);
                    context.startActivity(intent);
                }
            });

        }
    }
}
