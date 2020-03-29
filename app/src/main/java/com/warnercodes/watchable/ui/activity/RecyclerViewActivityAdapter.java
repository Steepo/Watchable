package com.warnercodes.watchable.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class RecyclerViewActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> dataList;
    public int typeReq;
    Context mContex;

    public RecyclerViewActivityAdapter(List<Movie> dataList, int typeReq) {
        this.dataList = dataList;
        this.typeReq = typeReq;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(typeReq == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advice_film, parent, false);
            return new ViewHolder1(view);
        }
        else{
            //if(typeReq == 2) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_activities, parent, false);
                return new ViewHolder2(view);
            }
            /*else{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover_similar, parent, false);
                return new ViewHolder3(view);
            }
        }*/
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(typeReq == 1) {
            ViewHolder1 viewHolder = (ViewHolder1) holder;
            mContex = viewHolder.img_advice.getContext();
            //Recent activities
            Glide.with(mContex).load(dataList.get(position).getCopertina()).into(viewHolder.img_advice);

            // Advice movie
            TextView textView = viewHolder.title_advice;
            TextView textView1 = viewHolder.trama_adv;
            textView.setText(dataList.get(position).getTitle());
            textView1.setText(dataList.get(position).getTrama());

            List<String> generi = new ArrayList<String>(dataList.get(position).getGeneri());

            Chip chip1 = viewHolder.chip1;
            Chip chip2 = viewHolder.chip2;
            Chip chip3 = viewHolder.chip3;

            if(generi.size() != 0)
                chip1.setText(generi.get(0));
            else chip1.setVisibility(View.INVISIBLE);

            if(generi.size() != 1)
                chip2.setText(generi.get(1));
            else chip2.setVisibility(View.INVISIBLE);

            if(generi.size() != 2)
                chip3.setText(generi.get(2));
            else chip3.setVisibility(View.INVISIBLE);
        }
        else{
            if(typeReq == 2) {
                ViewHolder2 viewHolder = (ViewHolder2) holder;
                mContex = viewHolder.img_movie.getContext();
                Glide.with(mContex).load(dataList.get(position).getCopertina()).into(viewHolder.img_movie);
            }
            /*else
            {
                ViewHolder3 viewHolder = (ViewHolder3) holder;
                mContex = viewHolder.img_similar_movie.getContext();
                Glide.with(mContex).load(dataList.get(position).getCopertina()).into(viewHolder.img_similar_movie);
            }*/
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        private ImageView img_movie;

        private ImageView img_advice;
        private TextView title_advice;
        private TextView trama_adv;
        private Chip chip1;
        private Chip chip2;
        private Chip chip3;
        ViewHolder1(View view) {
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

    class ViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView img_movie;
        ViewHolder2(View view) {
            super(view);
            this.img_movie = view.findViewById(R.id.img_movie);
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
