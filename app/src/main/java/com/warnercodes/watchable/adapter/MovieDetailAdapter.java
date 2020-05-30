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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.Review;
import com.warnercodes.watchable.activity.ReviewActivity;
import com.warnercodes.watchable.databinding.ItemMovieDetailsBinding;
import com.warnercodes.watchable.databinding.TitleRecyclerviewBinding;
import com.warnercodes.watchable.databinding.AdviceRecyclerviewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.warnercodes.watchable.Costants.API_KEY;
import static com.warnercodes.watchable.Costants.LANG;


public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    private List<Review> reviewList;
    private int movieId;
    private Movie movie;
    private String imdbID;

    public MovieDetailAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public void add(int position, Movie item) {
        movies.add(position, item);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieDetailsBinding view = ItemMovieDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        context = ((MovieDetailsViewHolder) holder).main_cover.getContext();
        final MovieDetailsViewHolder viewHolder = (MovieDetailsViewHolder) holder;
        movie = movies.get(position);

        movieId = movie.getMovieId();
        imdbID = movie.getImdbId();

        viewHolder.main_title.setText(movie.getTitle());
        viewHolder.original_title.setText(String.format("%s (original title)", movie.getOriginal_title()));
        int runtime = movie.getRuntime();
        String year = movie.getReleaseDate().substring(0, 4);
        viewHolder.year_runtime.setText(String.format("%s %dh %dm", year, runtime / 60, runtime % 60));
        viewHolder.trama.setText(movie.getOverview());

        viewHolder.btShowmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.trama.setMaxLines(Integer.MAX_VALUE);
                viewHolder.btShowmore.setVisibility(View.INVISIBLE);
            }
        });

        viewHolder.more_reviews.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("reviews", (Serializable) reviewList);
                context.startActivity(intent);
            }
        });

        //create chips from genres
        createChips(viewHolder);

        // set the main cover
        Glide.with(context).load(movie.getCopertina()).into(viewHolder.main_cover);

        viewHolder.casttextview.setText("Cast");
        viewHolder.similarTexview.setText("Similar");
        viewHolder.reviewstextview.setText("User Reviews");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(RequestCast(viewHolder));
        requestQueue.add(RequestSimilar(viewHolder));
        requestQueue.add(RequestTrailer(viewHolder));
        requestQueue.add(RequestOMdb(viewHolder));
        requestQueue.add(RequestReviews(viewHolder));

    }

    private void createChips(MovieDetailsViewHolder viewHolder) {
        List<String> generi = new ArrayList<String>(movie.getGeneri());
        for (String genere : generi) {
            Chip chip = new Chip(context);
            chip.setText(genere);
            viewHolder.chipGroup.addView(chip);
        }
    }

    private JsonObjectRequest RequestOMdb(final MovieDetailsViewHolder viewHolder) {
        String OMdbUrl = "https://www.omdbapi.com/?i=" + movie.getImdbId() + "&apikey=f435a572";
        JsonObjectRequest OMdbRequest = new JsonObjectRequest(Request.Method.GET, OMdbUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movie.addOMdbInfo(response);
                        Log.i("Director", "Entrato");
                        viewHolder.director.setText(movie.getDirector());
                        viewHolder.writers.setText(movie.getWriters());
                        viewHolder.awards.setText(movie.getAwards());
                        String metascoreScore = movie.getMetascoreScore();
                        if (metascoreScore.equals("N/A")) {
                            viewHolder.metascoreScore.setTextColor(context.getResources().getColor(R.color.black));
                            viewHolder.metascoreScore.setTextSize(18);
                            viewHolder.metascoreView.setBackgroundColor(context.getResources().getColor(R.color.meta_giallo));
                        } else {
                            int score = Integer.parseInt(metascoreScore);
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

                        }
                        viewHolder.metascoreScore.setText(metascoreScore);
                        viewHolder.metascoretext.setText("Metascore");
                        viewHolder.imdb_view.setVisibility(View.VISIBLE);
                        viewHolder.imdbScore.setText(movie.getImdbRating());
                        viewHolder.imdbScoreTen.setText("/10");

                        //TODO: rotten if null
                        if (movie.getRottenScore() != null) {
                            Integer rottenScore = Integer.parseInt(movie.getRottenScore().split("%")[0]);
                            if (rottenScore > 60)
                                viewHolder.rottenIcon.setBackground(context.getResources().getDrawable(R.drawable.fresh));
                            else
                                viewHolder.rottenIcon.setBackground(context.getResources().getDrawable(R.drawable.rotten));
                            viewHolder.rottenScore.setText(movie.getRottenScore());
                        } else {
                            viewHolder.rottenIcon.setBackground(context.getResources().getDrawable(R.drawable.fresh));
                            viewHolder.rottenScore.setText("N/A");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Director", error.getMessage());
            }
        });
        return OMdbRequest;
    }

    private JsonObjectRequest RequestReviews(final MovieDetailsViewHolder viewHolder) {
        String reviewsUrl = "https://imdb8.p.rapidapi.com/title/get-user-reviews?tconst=" + imdbID;
        JsonObjectRequest requestReviews = new JsonObjectRequest
                (Request.Method.GET, reviewsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            viewHolder.progressBar.setVisibility(View.INVISIBLE);
                            viewHolder.more_reviews.setVisibility(View.VISIBLE);
                            reviewList = new ArrayList<Review>();
                            RecyclerView recyclerView = viewHolder.reviewsRecyclerview;
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            //List<Cast> dataset = new ArrayList<Cast>();
                            HorizontalAdapter adapter = new HorizontalAdapter(context);
                            recyclerView.setAdapter(adapter);
                            JSONArray array = response.getJSONArray("reviews");
                            for (int index = 0; index < array.length(); index++) {
                                try {
                                    String author = array.getJSONObject(index).getJSONObject("author").getString("displayName");
                                    String text = array.getJSONObject(index).getString("reviewText");
                                    String title = array.getJSONObject(index).getString("reviewTitle");

                                    int score = 0;
                                    if (array.getJSONObject(index).has("authorRating"))
                                        score = array.getJSONObject(index).getInt("authorRating");

                                    JSONObject votes = array.getJSONObject(index).getJSONObject("interestingVotes");

                                    int voteup = 0;
                                    if (votes.has("up"))
                                        voteup = votes.getInt("up");

                                    int votedown = 0;
                                    if (votes.has("down"))
                                        votedown = votes.getInt("down");

                                    String rawDate = array.getJSONObject(index).getString("submissionDate");
                                    Date date = null;
                                    date = new SimpleDateFormat("yyy-MM-dd").parse(rawDate);
                                    reviewList.add(index, new Review(author, text, title, score, voteup, votedown, date));
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                    index--;
                                }
                            }
                            for (int j = 0; j < reviewList.size() && j < 5; j++) {
                                adapter.addReview(j, reviewList.get(j));
                                adapter.notifyDataSetChanged();
                            }
                            if (reviewList.size() < 5)
                                viewHolder.more_reviews.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("REVIEWS", String.valueOf(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "imdb8.p.rapidapi.com");
                params.put("x-rapidapi-key", "bc1890fdb0msh6891cde9c049b25p1968fcjsn62cb3d35f800");

                return params;
            }
        };
        return requestReviews;
    }

    private JsonObjectRequest RequestCast(final MovieDetailsViewHolder viewHolder) {
        String castUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY;
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
        return requestCast;
    }

    private JsonObjectRequest RequestTrailer(final MovieDetailsViewHolder viewHolder) {
        String videosUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
        JsonObjectRequest trailerRequest = new JsonObjectRequest
                (Request.Method.GET, videosUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("results");
                            if (array.length() > 0) {
                                for (int index = 0; index < 4; index++) {
                                    if (array.getJSONObject(index).getString("type").equals("Trailer")) {
                                        String youtubekey = array.getJSONObject(index).getString("key");
                                        String title = array.getJSONObject(index).getString("name");
                                        movies.get(0).setYoutubekey(youtubekey);
                                        Glide.with(context)
                                                .load("https://img.youtube.com/vi/" + youtubekey + "/maxresdefault.jpg")
                                                .error(Glide.with(context)
                                                        .load("https://img.youtube.com/vi/" + youtubekey + "/mqdefault.jpg"))
                                                .into(viewHolder.youtubeThumbnail);
                                    }
                                }
                            } else {
                                viewHolder.playicon.setVisibility(View.INVISIBLE);
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
        return trailerRequest;
    }

    private JsonObjectRequest RequestSimilar(final MovieDetailsViewHolder viewHolder) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
        JsonObjectRequest simialrRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                        adapter.add(index, movie.parseSingleMovieJson(array.getJSONObject(index), "recenti"));
                        adapter.notifyDataSetChanged();
                    }
                    if (array.length() == 0) {
                        viewHolder.similarView.titleRecyclerview.setVisibility(View.INVISIBLE);
                        viewHolder.similarView.itemTextview.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        return simialrRequest;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieDetailsViewHolder extends RecyclerView.ViewHolder {
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
        private TextView more_reviews;

        private TextView rottenScore;
        private ImageView rottenIcon;
        private ImageView imdb_view;
        private TextView imdbScore;
        private TextView imdbScoreTen;
        private TextView metascoretext;
        private AdviceRecyclerviewBinding reviewsView;
        private TextView reviewstextview;
        private RecyclerView reviewsRecyclerview;
        private ProgressBar progressBar;
        private ImageView playicon;

        MovieDetailsViewHolder(final ItemMovieDetailsBinding binding) {
            super(binding.getRoot());
            main_cover = binding.mainCover;
            main_title = binding.mainTitle;
            year_runtime = binding.yearRuntime;
            original_title = binding.originalTitle;
            trama = binding.trama;
            chipGroup = binding.containerGeneri;
            youtubeThumbnail = binding.youtubeThumbnail;
            playicon = binding.playIcon;
            btShowmore = binding.btShowmore;
            metascoreScore = binding.metascoreScore;
            similarView = binding.similarView;
            similarTexview = similarView.itemTextview;
            similarRecyclerview = similarView.titleRecyclerview;
            progressBar = binding.progressBar;

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
            reviewsRecyclerview = reviewsView.adviceTitleRecyclerview;
            more_reviews = binding.loadMoreReviews;

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


