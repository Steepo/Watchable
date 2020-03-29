package com.warnercodes.watchable;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_details, parent,false);
        CoverSimilarViewHolder viewHolder = new CoverSimilarViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        context = ((CoverSimilarViewHolder) holder).main_cover.getContext();
        final CoverSimilarViewHolder viewHolder = (CoverSimilarViewHolder) holder;
        final int movieId = movies.get(position).getMovieId();
        // Add Similar movies
        final List<ImageView> imageViewList = new ArrayList<ImageView>();
        imageViewList.add(viewHolder.similar1);
        imageViewList.add(viewHolder.similar2);
        imageViewList.add(viewHolder.similar3);
        imageViewList.add(viewHolder.similar4);

        imageViewList.get(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = movies.get(position).getSimilar().get(0);
                Log.i("ID", String.valueOf(id));
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", id);
                context.startActivity(intent);
            }
        });
        imageViewList.get(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = movies.get(position).getSimilar().get(1);
                Log.i("ID", String.valueOf(id));
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", id);
                context.startActivity(intent);
            }
        });
        imageViewList.get(2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = movies.get(position).getSimilar().get(2);
                Log.i("ID", String.valueOf(id));
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", id);
                context.startActivity(intent);
            }
        });
        imageViewList.get(3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = movies.get(position).getSimilar().get(3);
                Log.i("ID", String.valueOf(id));
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", id);
                context.startActivity(intent);
            }
        });

        viewHolder.main_title.setText(movies.get(position).getTitle());
        viewHolder.trama.setText(movies.get(position).getOverview());
        final ImageView youtubeThumbnail = viewHolder.youtubeThumbnail;

        List<String> generi = new ArrayList<String>(movies.get(position).getGeneri());
        for (String genere: generi) {
            Chip chip = new Chip(context);
            chip.setText(genere);
            viewHolder.chipGroup.addView(chip);
        }
        Glide.with(context).load(movies.get(position).getCopertina()).into(viewHolder.main_cover);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //gets similar movies
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"/similar?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
        final JsonObjectRequest simialrRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Movie movie = new Movie();
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int index = 0; index < 4; index++) {
                                String poster1 = "https://image.tmdb.org/t/p/w500" + array.getJSONObject(index).getString("poster_path");
                                Glide.with(context).load(poster1).into(imageViewList.get(index));
                                movies.get(position).addSimilar(array.getJSONObject(index).getInt("id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MovieDetailActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", String.valueOf(error));
                    }
                });
        String videosUrl = "https://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
        final JsonObjectRequest videosRequest = new JsonObjectRequest
                (Request.Method.GET, videosUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int index = 0; index < 4; index++) {
                                if (array.getJSONObject(index).getString("type").equals("Trailer")) {
                                    String youtubekey = array.getJSONObject(index).getString("key");
                                    String title = array.getJSONObject(index).getString("name");
                                    movies.get(0).setYoutubekey(youtubekey);
                                    Glide.with(context)
                                            .load("https://img.youtube.com/vi/" + youtubekey + "/maxresdefault.jpg")
                                            .error(Glide.with(context)
                                                    .load("https://img.youtube.com/vi/" + youtubekey + "/mqdefault.jpg"))
                                            .into(youtubeThumbnail);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MovieDetailActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", String.valueOf(error));
                    }
                });

        requestQueue.add(simialrRequest);
        requestQueue.add(videosRequest);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class CoverSimilarViewHolder extends RecyclerView.ViewHolder {
        private ImageView main_cover;
        private ImageView similar1;
        private ImageView similar2;
        private ImageView similar3;
        private ImageView similar4;
        private TextView main_title;
        private TextView trama;
        private ChipGroup chipGroup;
        private ImageView youtubeThumbnail;
        private ImageView favorite;
        CoverSimilarViewHolder(View view) {
            super(view);
            this.main_cover = view.findViewById(R.id.main_cover);
            this.main_title = view.findViewById(R.id.main_title);
            this.trama = view.findViewById(R.id.trama);
            this.similar1 = view.findViewById(R.id.similar1);
            this.similar2 = view.findViewById(R.id.similar2);
            this.similar3 = view.findViewById(R.id.similar3);
            this.similar4 = view.findViewById(R.id.similar4);
            this.chipGroup = view.findViewById(R.id.container_generi);
            this.youtubeThumbnail = view.findViewById(R.id.youtube_thumbnail);
            this.favorite = view.findViewById(R.id.favorite);
            youtubeThumbnail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = movies.get(getAdapterPosition()).getYoutubekey();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
            favorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Write a message to the database
                    // Access a Cloud Firestore instance from your Activity

                    Map<String, String> userInfos = new HashMap<>();
                    userInfos.put("Cognome", "Pippo");
                    userInfos.put("Nome", "Rossi");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference user = db.collection("utenti").document("7OUM3aVwSdD0J3MS1uor");
                    user.set(userInfos);

                    Movie movieRef = movies.get(getAdapterPosition());

                    Map<String, Object> movieInfos = new HashMap<>();
                    movieInfos.put("title", movieRef.getTitle());
                    movieInfos.put("movieId", movieRef.getMovieId());
                    movieInfos.put("copertina", movieRef.getCopertina());
                    movieInfos.put("runtime", movieRef.getRuntime());
                    DocumentReference favorites = user.collection("favorites").document(String.valueOf(movieRef.getMovieId()));
                    favorites.set(movieInfos);
                    favorite.setImageResource(R.drawable.ic_favorite_full_24dp);
                }
            });
        }
    }
}


