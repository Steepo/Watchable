package com.warnercodes.watchable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class HorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> dataList;
    private List<Cast> dataListCast;

    public HorizontalAdapter(List<Movie> dataList) {
        this.dataList = dataList;
    }

    public HorizontalAdapter(List<Cast> dataListCast, int i) {
        this.dataListCast = dataListCast;
    }

    public void add(int position, Movie item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void addCast(int position, Cast item) {
        dataListCast.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Movie item) {
        int position = dataList.indexOf(item);
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    private static int RECENTI = 1;
    private static int CONSIGLIATI = 2;
    private static int SIMILI = 3;
    private static int CINEMA = 4;
    private static int POPOLARI = 5;
    private static int ARRIVO = 6;
    private static int VOTATI = 7;
    private static int CAST = 8;




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
            return new SimiliViewHolder(view);
        }
        if (viewType == 4) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new SimiliViewHolder(view);
        }
        if (viewType == 5) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new SimiliViewHolder(view);
        }
        if (viewType == 6) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new SimiliViewHolder(view);
        }
        if (viewType == 7) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent, parent, false);
            return new SimiliViewHolder(view);
        }
        if (viewType == 8) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
            return new CastViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == RECENTI) {
            RecentiViewHolder viewHolder = (RecentiViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == CONSIGLIATI) {
            AdviceViewHolder viewHolder = (AdviceViewHolder) holder;
            Context context = viewHolder.img_advice.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_advice);
            TextView textView = viewHolder.title_advice;
            TextView textView1 = viewHolder.trama_adv;
            textView.setText(dataList.get(position).getTitle());
            textView1.setText(dataList.get(position).getTrama());
            textView1.setMovementMethod(new ScrollingMovementMethod());

            List<String> generi = new ArrayList<String>(dataList.get(position).getGeneri());

            Chip chip1 = viewHolder.chip1;
            Chip chip2 = viewHolder.chip2;
            Chip chip3 = viewHolder.chip3;

            if (generi.size() != 0)
                chip1.setText(generi.get(0));
            else chip1.setVisibility(View.INVISIBLE);

            if (generi.size() != 1)
                chip2.setText(generi.get(1));
            else chip2.setVisibility(View.INVISIBLE);

            if (generi.size() != 2)
                chip3.setText(generi.get(2));
            else chip3.setVisibility(View.INVISIBLE);
        }

        if (getItemViewType(position) == SIMILI) {
            SimiliViewHolder viewHolder = (SimiliViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == CINEMA) {
            SimiliViewHolder viewHolder = (SimiliViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == POPOLARI) {
            SimiliViewHolder viewHolder = (SimiliViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == ARRIVO) {
            SimiliViewHolder viewHolder = (SimiliViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == VOTATI) {
            SimiliViewHolder viewHolder = (SimiliViewHolder) holder;
            Context context = viewHolder.img_movie.getContext();
            Glide.with(context).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
        }

        if (getItemViewType(position) == CAST) {
            CastViewHolder viewHolder = (CastViewHolder) holder;
            Context context = viewHolder.img_cast.getContext();
            TextView textView = viewHolder.person_name;
            textView.setText(dataListCast.get(position).getName());

            Glide.with(context).load(dataListCast.get(position).getCopertina()).into(viewHolder.img_cast);
        }

    }

    @Override
    public int getItemViewType(int position) {
        String tipo;
        if(dataList != null)   tipo = dataList.get(position).getTipo();
        else tipo = dataListCast.get(position).getTipo();
        if (tipo.equals("recenti"))
            return RECENTI;
        if (tipo.equals("consigliati"))
            return CONSIGLIATI;
        if (tipo.equals("simili"))
            return SIMILI;
        if (tipo.equals("cinema"))
            return CINEMA;
        if (tipo.equals("popolari"))
            return POPOLARI;
        if (tipo.equals("arrivo"))
            return ARRIVO;
        if (tipo.equals("votati"))
            return VOTATI;
        if (tipo.equals("cast"))
            return CAST;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if(dataList != null)
        return dataList.size();
        else {
                return dataListCast.size();
        }
    }

    class RecentiViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        RecentiViewHolder(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);
        }
    }

    class AdviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        private ImageView img_advice;
        private TextView title_advice;
        private TextView trama_adv;
        private Chip chip1;
        private Chip chip2;
        private Chip chip3;

        AdviceViewHolder(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);

            this.img_advice = view.findViewById(R.id.img_advice);
            this.title_advice = view.findViewById(R.id.title_advice);
            this.trama_adv = view.findViewById(R.id.trama_advice);

            this.chip1 = view.findViewById(R.id.chip1);
            this.chip2 = view.findViewById(R.id.chip2);
            this.chip3 = view.findViewById(R.id.chip3);

        }

    }

    class SimiliViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        SimiliViewHolder(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);
        }
    }

    class CastViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_cast;
        private TextView person_name;

        CastViewHolder(View view) {
            super(view);
            this.img_cast = view.findViewById(R.id.profile_path);
            this.person_name = view.findViewById(R.id.person_name);
        }
    }
}