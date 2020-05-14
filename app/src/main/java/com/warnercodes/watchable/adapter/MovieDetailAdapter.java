package com.warnercodes.watchable.adapter;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.Review;
import com.warnercodes.watchable.databinding.ItemMovieDetailsBinding;
import com.warnercodes.watchable.databinding.TitleRecyclerviewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<Movie> movies;

    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    private List<String> myUrls;

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
        ItemMovieDetailsBinding view = ItemMovieDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CoverSimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        context = ((CoverSimilarViewHolder) holder).main_cover.getContext();
        final CoverSimilarViewHolder viewHolder = (CoverSimilarViewHolder) holder;
        final int movieId = movies.get(position).getMovieId();


        viewHolder.main_title.setText(movies.get(position).getTitle());
        viewHolder.original_title.setText(String.format("%s (original title)", movies.get(position).getOriginal_title()));
        int runtime = movies.get(position).getRuntime();
        int hours = runtime / 60;
        int minutes = runtime % 60;
        String year = movies.get(position).getReleaseDate().substring(0, 4);
        viewHolder.year_runtime.setText(String.format("%s %dh %dm", year, hours, minutes));
        viewHolder.trama.setText(movies.get(position).getOverview());

        viewHolder.btShowmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.trama.setMaxLines(Integer.MAX_VALUE);
                viewHolder.btShowmore.setVisibility(View.INVISIBLE);
            }
        });

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
        viewHolder.similarTexview.setText("Similar");
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"/similar?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
        final JsonObjectRequest simialrRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RecyclerView recyclerView = viewHolder.similarRecyclerview;
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            List<Movie> dataset = new ArrayList<Movie>();
                            System.out.println("Secondo inizio");
                            HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                            recyclerView.setAdapter(adapter);

                            JSONArray array = response.getJSONArray("results");

                            for (int index = 0; index < array.length(); index++) {
                                Movie movie = new Movie();
                                adapter.add(index, movie.parseSingleMovieJson(array.getJSONObject(index), "simili"));
                                adapter.notifyDataSetChanged();
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


        viewHolder.casttextview.setText("Cast");

        String castUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=db18c03be648dd161624fabd8596021a";
        JsonObjectRequest requestCast = new JsonObjectRequest(Request.Method.GET, castUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RecyclerView recyclerView = viewHolder.castRecyclerview;
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            //List<Cast> dataset = new ArrayList<Cast>();
                            System.out.println("Secondo inizio");
                            HorizontalAdapter adapter = new HorizontalAdapter(context);
                            recyclerView.setAdapter(adapter);
                            JSONArray array = response.getJSONArray("cast");
                            for (int index = 0; index < array.length(); index++) {
                                Cast cast = new Cast();
                                adapter.addCast(index, cast.parseCastJson(array.getJSONObject(index), "cast"));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        String OMdbUrl = "https://www.omdbapi.com/?i=" + movies.get(position).getImdbId() + "&apikey=f435a572";
        JsonObjectRequest OMdbRequest = new JsonObjectRequest(Request.Method.GET, OMdbUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movies.get(position).addOMdbInfo(response);
                        Log.i("Director", "Entrato");
                        viewHolder.director.setText(movies.get(position).getDirector());
                        viewHolder.writers.setText(movies.get(position).getWriters());
                        viewHolder.awards.setText(movies.get(position).getAwards());
                        int score = Integer.parseInt(movies.get(position).getMetascoreScore());
                        if (score >= 60) {
                            //verde
                            viewHolder.metascoreScore.setTextColor(context.getResources().getColor(R.color.white));
                            viewHolder.metascoreView.setBackgroundColor(context.getResources().getColor(R.color.meta_verde));
                        } else if (score >= 40) {
                            //giallo
                            viewHolder.metascoreScore.setTextColor(context.getResources().getColor(R.color.black));
                            viewHolder.metascoreView.setBackgroundColor(context.getResources().getColor(R.color.meta_giallo));
                        } else {
                            //rosso
                            viewHolder.metascoreScore.setTextColor(context.getResources().getColor(R.color.white));
                            viewHolder.metascoreView.setBackgroundColor(context.getResources().getColor(R.color.meta_rosso));
                        }
                        viewHolder.metascoreScore.setText(String.valueOf(score));
                        viewHolder.metascoretext.setText("Metascore");

                        viewHolder.imdb_view.setVisibility(View.VISIBLE);
                        viewHolder.imdbScore.setText(movies.get(position).getImdbRating());
                        viewHolder.imdbScoreTen.setText("/10");

                        Integer rottenScore = Integer.parseInt(movies.get(position).getRottenScore().substring(0, 2));
                        if (rottenScore > 60)
                            viewHolder.rottenIcon.setBackground(context.getResources().getDrawable(R.drawable.fresh));
                        else
                            viewHolder.rottenIcon.setBackground(context.getResources().getDrawable(R.drawable.rotten));

                        viewHolder.rottenScore.setText(movies.get(position).getRottenScore());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Director", error.getMessage());
            }
        });

        viewHolder.reviewstextview.setText("Reviews");
        String reviewsUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=db18c03be648dd161624fabd8596021a";
        JsonObjectRequest requestReviews = new JsonObjectRequest(Request.Method.GET, reviewsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RecyclerView recyclerView = viewHolder.reviewsRecyclerview;
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            //List<Cast> dataset = new ArrayList<Cast>();
                            HorizontalAdapter adapter = new HorizontalAdapter(context);
                            recyclerView.setAdapter(adapter);
                            JSONArray array = response.getJSONArray("results");
                            for (int index = 0; index < 5; index++) {
                                Log.i("REVIEW", array.getJSONObject(index).getString("content"));
                                adapter.addReview(index, new Review(array.getJSONObject(index).getString("author"), array.getJSONObject(index).getString("content")));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });



        requestQueue.add(requestCast);
        requestQueue.add(simialrRequest);
        requestQueue.add(videosRequest);
        requestQueue.add(OMdbRequest);
        requestQueue.add(requestReviews);

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
        private ItemMovieDetailsBinding binding;
        private ImageView main_cover;
        private TextView main_title;
        private TextView original_title;
        private TextView year_runtime;
        private TextView trama;
        private ChipGroup chipGroup;
        private ImageView youtubeThumbnail;
        private TextView btShowmore;
        private TextView metascoreScore;
        private View metascoreView;


        private TitleRecyclerviewBinding similarView;
        private TextView similarTexview;
        private RecyclerView similarRecyclerview;

        private TitleRecyclerviewBinding castView;
        private TextView casttextview;
        private RecyclerView castRecyclerview;
        private TextView director;
        private TextView writers;
        private TextView awards;
        private MaterialButton watched;

        private TextView rottenScore;
        private ImageView rottenIcon;
        private ImageView imdb_view;
        private TextView imdbScore;
        private TextView imdbScoreTen;
        private TextView metascoretext;
        private TitleRecyclerviewBinding reviewsView;
        private TextView reviewstextview;
        private RecyclerView reviewsRecyclerview;

        CoverSimilarViewHolder(final ItemMovieDetailsBinding binding) {
            super(binding.getRoot());
            main_cover = binding.mainCover;
            main_title = binding.mainTitle;
            year_runtime = binding.yearRuntime;
            original_title = binding.originalTitle;
            trama = binding.trama;
            chipGroup = binding.containerGeneri;
            youtubeThumbnail = binding.youtubeThumbnail;
            btShowmore = binding.btShowmore;
            metascoreScore = binding.metascoreScore;
            similarView = binding.similarView;
            similarTexview = similarView.itemTextview;
            similarRecyclerview = similarView.titleRecyclerview;

            castView = binding.castView;
            casttextview = castView.itemTextview;
            castRecyclerview = castView.titleRecyclerview;
            director = binding.director;
            writers = binding.writers;
            awards = binding.awardsText;
            rottenIcon = binding.rottenIcon;

            rottenScore = binding.rottenScore;
            imdbScore = binding.imdbScore;
            imdbScoreTen = binding.imdbScoreTen;
            imdb_view = binding.imdbView;
            metascoretext = binding.metascoretext;
            metascoreView = binding.metascoreView;
            reviewsView = binding.reviewsView;
            reviewstextview = reviewsView.itemTextview;
            reviewsRecyclerview = reviewsView.titleRecyclerview;


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
        }
    }
}


