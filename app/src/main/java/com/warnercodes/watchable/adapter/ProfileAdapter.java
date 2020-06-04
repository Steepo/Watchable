package com.warnercodes.watchable.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.activity.SettingsActivity;
import com.warnercodes.watchable.databinding.ItemProfileBinding;
import com.warnercodes.watchable.databinding.TitleRecyclerviewBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> dataList;
    public int typeReq;
    Context mContex;
    private Context context;

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
    private static int WATCHED = 2;
    private static int WATCHLIST = 3;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PROFILO) {
            ItemProfileBinding view = ItemProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ProfileViewHolder(view);
        }
        if (viewType == WATCHED || viewType == WATCHLIST) {
            TitleRecyclerviewBinding view = TitleRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        context = holder.itemView.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences("infos", Context.MODE_PRIVATE);
        final String email = sharedPref.getString("email", null);
        String uid = sharedPref.getString("uid", null);
        final String fullname = sharedPref.getString("name", null);
        final String profileUrl = sharedPref.getString("photourl", null);

        //Log.i("Glide", profileUrl);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user = db.collection("utenti").document(uid);
        CollectionReference watched = user.collection("watched");
        CollectionReference watchlist = user.collection("watchlist");
        if (getItemViewType(position) == PROFILO) {
            context = ((ProfileViewHolder) holder).avatar_profile.getContext();
            final ProfileViewHolder viewHolder = (ProfileViewHolder) holder;

            //TODO: add image from firebase
            Glide.with(context).load(profileUrl).into(viewHolder.avatar_profile);

            user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    viewHolder.profile_mail.setText(email);
                    viewHolder.profile_name.setText(fullname);
                }
            });

            watched.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        int total = 0;
                        for (DocumentSnapshot document : documents) {
                            long runtime = (long) document.get("runtime");
                            total += runtime;
                            //Log.i("Profile", String.valueOf(document.get("runtime")));
                        }
                        int hours = total / 60;
                        viewHolder.hours_spent.setText("You've watched " + hours + " hours of movies");
                    }
                }
            });
        }

        if (getItemViewType(position) == WATCHED) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Guardati");
            final RecyclerView recyclerView = viewHolder.item_recylerview;
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            final List<Movie> dataset = new ArrayList<Movie>();

            watched.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        int index = 0;
                        HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                        recyclerView.setAdapter(adapter);
                        adapter.clear();
                        for (DocumentSnapshot document : documents) {
                            Movie movie = new Movie();
                            movie.setCopertinaFull(document.getString("copertina"));
                            movie.setMovieId(document.getLong("movieId").intValue());
                            movie.setRuntime((document.getLong("runtime").intValue()));
                            movie.setTitle(document.getString("title"));
                            movie.setTipo("recenti");
                            adapter.add(index, movie);
                            adapter.notifyDataSetChanged();
                            index++;
                        }
                    }
                }
            });
        }

        if (getItemViewType(position) == WATCHLIST) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Da guardare");
            final RecyclerView recyclerView = viewHolder.item_recylerview;
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            final List<Movie> dataset = new ArrayList<Movie>();

            watchlist.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        int index = 0;
                        HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                        recyclerView.setAdapter(adapter);
                        adapter.clear();
                        for (DocumentSnapshot document : documents) {
                            Movie movie = new Movie();
                            movie.setCopertinaFull(document.getString("copertina"));
                            movie.setMovieId(document.getLong("movieId").intValue());
                            movie.setRuntime((document.getLong("runtime").intValue()));
                            movie.setTitle(document.getString("title"));
                            movie.setTipo("recenti");
                            adapter.add(index, movie);
                            adapter.notifyDataSetChanged();
                            index++;
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        int tipo = dataList.get(position).getType();
        if (tipo == 1)
            return PROFILO;
        if (tipo == 2)
            return WATCHED;
        if (tipo == 3)
            return WATCHLIST;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_textview;
        private RecyclerView item_recylerview;

        ViewHolder(TitleRecyclerviewBinding binding) {
            super(binding.getRoot());
            this.item_textview = binding.itemTextview;
            this.item_recylerview = binding.titleRecyclerview;
        }
    }

    private class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView profile_name;
        private TextView profile_mail;
        private TextView hours_spent;
        private ImageView avatar_profile;

        ProfileViewHolder(ItemProfileBinding binding) {
            super(binding.getRoot());
            this.avatar_profile = binding.avatarProfile;
            this.hours_spent = binding.hoursSpent;
            this.profile_mail = binding.profileMail;
            this.profile_name = binding.profileName;
            binding.settingsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SettingsActivity.class);
                    context.startActivity(intent);
                }
            });
        }

    }
}
