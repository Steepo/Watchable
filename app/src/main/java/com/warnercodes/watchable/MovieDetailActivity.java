package com.warnercodes.watchable;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warnercodes.watchable.adapter.MovieDetailAdapter;
import com.warnercodes.watchable.databinding.ActivityMovieDetailBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieDetailAdapter movieDetailAdapter;
    private List<Movie> mDataset;
    private RecyclerView.LayoutManager mLayoutManager;
    private int movieId;
    private Context context;
    private ActivityMovieDetailBinding binding;
    private FirebaseFirestore db;
    private DocumentReference user;
    private DocumentReference watchlist;
    private DocumentReference watched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //getting intent infos
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId", 0);
        context = this;

        //firebase init
        db = FirebaseFirestore.getInstance();
        user = db.collection("utenti").document("7OUM3aVwSdD0J3MS1uor");

        watchlist = user.collection("watchlist").document(String.valueOf(movieId));
        watched = user.collection("watched").document(String.valueOf(movieId));

        // showing back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // rv init
        recyclerView = binding.detailsRecyclerview;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        //API db18c03be648dd161624fabd8596021a
        if (movieId == 0)
            requestMovieInfo(181812); //Star Wars The Rise of the Skywalker
        else
            requestMovieInfo(movieId);
        binding.watchlistFab.shrink();
        binding.watchedFab.shrink();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.detailsRecyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    binding.watchlistFab.shrink();
                    binding.watchedFab.shrink();
                    binding.watchedFab.setVisibility(View.INVISIBLE);
                }
            });
        }

        binding.watchlistFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.watchlistFab.isExtended()) {
                    binding.watchlistFab.extend();
                    binding.watchedFab.extend();
                    binding.watchedFab.setVisibility(View.VISIBLE);
                } else {
                    final Movie movieRef = mDataset.get(0);
                    if (movieRef.isInWatchlist()) {
                        watchlist.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp);
                                binding.watchlistFab.setIcon(removeIcon);
                                binding.watchlistFab.setText(R.string.add_watchlist);
                                movieRef.setInWatchlist(false);
                                binding.watchlistFab.shrink();
                                binding.watchedFab.shrink();
                                binding.watchedFab.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Map<String, Object> movieInfos = new HashMap<>();
                        movieInfos.put("title", movieRef.getTitle());
                        movieInfos.put("movieId", movieRef.getMovieId());
                        movieInfos.put("copertina", movieRef.getCopertina());
                        movieInfos.put("runtime", movieRef.getRuntime());
                        watchlist.set(movieInfos).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp);
                                binding.watchlistFab.setIcon(removeIcon);
                                binding.watchlistFab.setText(R.string.remove_watchlist);
                                movieRef.setInWatchlist(true);
                                binding.watchlistFab.shrink();
                                binding.watchedFab.shrink();
                                binding.watchedFab.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }
        });

        binding.watchedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.watchlistFab.isExtended()) {
                    binding.watchlistFab.extend();
                    binding.watchedFab.extend();
                    binding.watchedFab.setVisibility(View.VISIBLE);
                } else {
                    final Movie movieRef = mDataset.get(0);
                    if (movieRef.isWatched()) {
                        watched.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp);
                                binding.watchedFab.setIcon(removeIcon);
                                binding.watchedFab.setText(R.string.add_watched);
                                movieRef.setWatched(false);
                                binding.watchlistFab.shrink();
                                binding.watchedFab.shrink();
                                binding.watchedFab.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Map<String, Object> movieInfos = new HashMap<>();
                        movieInfos.put("title", movieRef.getTitle());
                        movieInfos.put("movieId", movieRef.getMovieId());
                        movieInfos.put("copertina", movieRef.getCopertina());
                        movieInfos.put("runtime", movieRef.getRuntime());
                        watched.set(movieInfos).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp);
                                binding.watchedFab.setIcon(removeIcon);
                                binding.watchedFab.setText(R.string.remove_watched);
                                movieRef.setWatched(true);
                                binding.watchlistFab.shrink();
                                binding.watchedFab.shrink();
                                binding.watchedFab.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void requestMovieInfo(final int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=db18c03be648dd161624fabd8596021a&language=it-it";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Movie movie = new Movie();
                        movie.parseJson(response);
                        mDataset = movie.getMovie();
                        getSupportActionBar().setTitle(mDataset.get(0).getTitle());
                        movieDetailAdapter = new MovieDetailAdapter(mDataset);
                        recyclerView.setAdapter(movieDetailAdapter);
                        setFabStates();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MovieDetailActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void setFabStates() {
        //check if movie is already in favorite list
        DocumentReference watchlist = user.collection("watchlist").document(String.valueOf(movieId));
        watchlist.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Movie movie = mDataset.get(0);
                    if (document.exists()) {
                        // il film è già aggiunto alla lista preferiti
                        movie.setInWatchlist(true);
                        Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp);
                        binding.watchlistFab.setIcon(removeIcon);
                        binding.watchlistFab.setText(R.string.remove_watchlist);

                    } else {
                        // il film non è nella lista preferiti
                        movie.setInWatchlist(false);
                        Drawable addIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp);
                        binding.watchlistFab.setIcon(addIcon);
                        binding.watchlistFab.setText(R.string.add_watchlist);
                    }
                    binding.watchlistFab.setVisibility(View.VISIBLE);

                } else {
                    Log.d("ITEM", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference watched = user.collection("watched").document(String.valueOf(movieId));
        watched.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Movie movie = mDataset.get(0);
                    if (document.exists()) {
                        movie.setInWatchlist(true);
                        Drawable removeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp);
                        binding.watchedFab.setIcon(removeIcon);
                        binding.watchedFab.setText(R.string.remove_watched);

                    } else {
                        // il film non è nella lista preferiti
                        movie.setInWatchlist(false);
                        Drawable addIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp);
                        binding.watchedFab.setIcon(addIcon);
                        binding.watchedFab.setText(R.string.add_watched);
                    }
                } else {
                    Log.d("ITEM", "get failed with ", task.getException());
                }
            }
        });
    }


}