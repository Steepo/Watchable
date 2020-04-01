package com.warnercodes.watchable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> dataList;
    public int typeReq;
    Context mContex;

    public ProfileAdapter(List<ItemType> dataList) {
        this.dataList = dataList;
        this.typeReq = typeReq;
    }

    public void add(int position, ItemType item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Movie item) {
        int position = dataList.indexOf(item);
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    private static int PROFILO = 1;
    private static int PREFERITI = 2;
    private static int GUARDARE = 3;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
            return new ProfileViewHolder(view);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_recyclerview, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_recyclerview, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == PROFILO) {

        }
        if (getItemViewType(position) == PREFERITI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Preferiti");
        }
        if (getItemViewType(position) == GUARDARE) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Da guardare");
        }
    }

    @Override
    public int getItemViewType(int position) {
        int tipo = dataList.get(position).getType();
        if (tipo == 1)
            return PROFILO;
        if (tipo == 2)
            return PREFERITI;
        if (tipo == 3)
            return GUARDARE;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_textview;
        private RecyclerView item_recylerview;

        ViewHolder(View view) {
            super(view);
            this.item_textview = view.findViewById(R.id.item_textview);
            this.item_recylerview = view.findViewById(R.id.item_recylerview);
        }
    }

    private class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView profile_name;
        private TextView profile_mail;
        private TextView hours_spent;
        private ImageView settings_imageView;
        private ImageView avatar_profile;

        public ProfileViewHolder(View view) {
            super(view);
            this.avatar_profile = view.findViewById(R.id.avatar_profile);
            this.settings_imageView = view.findViewById(R.id.settings_imageView);
            this.hours_spent = view.findViewById(R.id.hours_spent);
            this.profile_mail = view.findViewById(R.id.profile_mail);
            this.profile_name = view.findViewById(R.id.profile_name);
        }
    }
}
