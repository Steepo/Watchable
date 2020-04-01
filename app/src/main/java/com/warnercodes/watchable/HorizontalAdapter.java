package com.warnercodes.watchable;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> dataList;
    private List<Cast> castList;

    public HorizontalAdapter(List<Movie> dataList) {
        this.dataList = dataList;
    }

    public HorizontalAdapter() {
        castList = new ArrayList<Cast>();
    }

    public void add(int position, Movie item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void addCast(int position, Cast item) {
        castList.add(position, item);
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
    private static int CAST = 4;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new RecentiViewHolder(view);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new AdviceViewHolder(view);
        }
        if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new RecentiViewHolder(view);
        }

        if (viewType == 4) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
            return new CastViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == COSIGLIATI) {

        }
        if (getItemViewType(position) == RECENTI) {
            RecentiViewHolder viewHolder = (RecentiViewHolder) holder;
            context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == SIMILI) {
            RecentiViewHolder viewHolder = (RecentiViewHolder) holder;
            context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }
        if (getItemViewType(position) == CAST) {
            CastViewHolder viewHolder = (CastViewHolder) holder;
            Context context = viewHolder.attore.getContext();
            Cast itemCast = castList.get(position);
            Glide.with(context).load(itemCast.getProfile_path()).into(viewHolder.cover);
            viewHolder.attore.setText(itemCast.getName());
            viewHolder.personaggio.setText(itemCast.getCharacter());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList != null) {
            String tipo = dataList.get(position).getTipo();
            if (tipo.equals("recenti"))
                return RECENTI;
            if (tipo.equals("consigliati"))
                return COSIGLIATI;
            if (tipo.equals("simili"))
                return SIMILI;
            if (tipo.equals("cast"))
                return CAST;
        }else {
            String tipo = castList.get(position).getTipo();
            if (tipo.equals("cast"))
                return CAST;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (dataList != null)
            return dataList.size();
        else
            return  castList.size();
    }

    class RecentiViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;
        RecentiViewHolder(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);
            img_movie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer id = dataList.get(getAdapterPosition()).getMovieId();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movieId", id);
                    context.startActivity(intent);
                }
            });
        }
    }

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        AdviceViewHolder(View view) {
            super(view);
        }
    }

    private class CastViewHolder extends RecyclerView.ViewHolder {
        private TextView attore;
        private TextView personaggio;
        private ImageView cover;
        public CastViewHolder(View view) {
            super(view);
            this.attore = view.findViewById(R.id.person_name);
            this.personaggio = view.findViewById(R.id.character_name);
            this.cover = view.findViewById(R.id.profile_path);
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
