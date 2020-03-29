package com.warnercodes.watchable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;


import com.bumptech.glide.Glide;

import java.util.List;


public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> dataList;

    public HorizontalAdapter(List<Movie> dataList) {
        this.dataList = dataList;
    }

    public void add(int position, Movie item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Movie item) {
        int position = dataList.indexOf(item);
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    private static int RECENTI = 1;
    private static int COSIGLIATI = 2;
    private static int SIMILI = 3;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new RecentiViewHolder(view);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advice, parent, false);
            return new AdviceViewHolder(view);
        }
        if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new RecentiViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == COSIGLIATI) {

        }
        if (getItemViewType(position) == RECENTI) {
            RecentiViewHolder viewHolder = (RecentiViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == SIMILI) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        String tipo = dataList.get(position).getTipo();
        if (tipo.equals("recenti"))
            return RECENTI;
        if (tipo.equals("consigliati"))
            return COSIGLIATI;
        if (tipo.equals("simili"))
            return SIMILI;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecentiViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;
        RecentiViewHolder(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);

        }
    }

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        AdviceViewHolder(View view) {
            super(view);
        }
    }

    /*class ViewHolder3 extends RecyclerView.ViewHolder {
        private ImageView img_similar_movie;
        ViewHolder3(View view) {
            super(view);
            this.img_similar_movie = view.findViewById(R.id.img_similar_movie);
        }
    }*/
}