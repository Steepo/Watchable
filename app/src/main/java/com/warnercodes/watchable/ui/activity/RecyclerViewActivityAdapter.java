package com.warnercodes.watchable.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.R;

import java.util.ArrayList;


public class RecyclerViewActivityAdapter extends RecyclerView.Adapter<RecyclerViewActivityAdapter.TestViewHolder> {

    private ArrayList<Movie> dataList;
    private Fragment mContex;

    public RecyclerViewActivityAdapter(ArrayList<Movie> dataList, Fragment mContex) {
        this.dataList = dataList;
        this.mContex = mContex;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recent_activities, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewActivityAdapter.TestViewHolder holder, int position) {
        final Movie data = dataList.get(position);
        holder.imgTest.setImageResource(data.getCopertina());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTest;

        public TestViewHolder(View itemView) {
            super(itemView);
            imgTest = (ImageView) itemView.findViewById(R.id.img_movie);
        }

    }
}