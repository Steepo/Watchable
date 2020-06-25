package com.warnercodes.watchable.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.warnercodes.watchable.Costants.API_KEY;
import static com.warnercodes.watchable.Costants.LANG;

public class AttivitaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> dataList;
    private Context context;
    private static final int MAX_ITEMS_RECYCLERVIEW = 8;
    private int itemsRecyclerView = 0;

    public AttivitaAdapter(Context context, List<ItemType> dataList) {
        this.context = context;
        this.dataList = dataList;
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
        if (viewType == RECENTI || viewType == ARRIVO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_without_title, parent, false);
            return new ViewHolderNoTitle(view);
        }
        if (viewType == CONSIGLIATI || viewType == SIMILI || viewType == POPOLARI || viewType == CAST || viewType == CINEMA || viewType == VOTATI) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_recyclerview, parent, false);
            return new ViewHolderNoCard(view);
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user = db.collection("utenti").document(uid);
        CollectionReference watchlist = user.collection("watchlist");
        CollectionReference watched = user.collection("watched");
        final List<Integer> listaFilmDaGuardare = new ArrayList<Integer>();
        final List<Integer> listaFilmGuardati = new ArrayList<Integer>();

        if (getItemViewType(position) == RECENTI) {

            final ViewHolderNoTitle viewHolder = (ViewHolderNoTitle) holder;
            viewHolder.imageView.setBackgroundResource(R.drawable.recently_bg);
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());

            final RecyclerView recyclerView = viewHolder.without_title_recyclerview;
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            final List<Movie> dataset = new ArrayList<Movie>();

            watchlist.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FIREBASE", "Listen failed.", e);
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.d("FIREBASE", "Current data: " + queryDocumentSnapshots.getDocuments());
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
                    } else {
                        Log.d("FIREBASE", "Current data: null");
                    }
                }
            });
        }

        if (getItemViewType(position) == CONSIGLIATI) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());
            watchlist.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FIREBASE", "Listen failed.", e);
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.d("FIREBASE", "Current data: " + queryDocumentSnapshots.getDocuments());
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Movie movie = new Movie();
                            listaFilmDaGuardare.add(document.getLong("movieId").intValue());
                        }
                    } else {
                        Log.d("FIREBASE", "Current data: null");
                    }
                }
            });

            watched.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FIREBASE", "Listen failed.", e);
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.d("FIREBASE", "Current data: " + queryDocumentSnapshots.getDocuments());
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Movie movie = new Movie();
                            listaFilmGuardati.add(document.getLong("movieId").intValue());
                        }
                        loadAdviceFilms(viewHolder, listaFilmDaGuardare, listaFilmGuardati);
                    } else {
                        Log.d("FIREBASE", "Current data: null");
                    }
                }
            });
        }

        if (getItemViewType(position) == SIMILI) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/11/similar?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.title_recyclerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                itemsRecyclerView = setMaxElemntsNumber(movie_array.length());
                                for (int index = 0; index < itemsRecyclerView; index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "recenti"));
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }

        if (getItemViewType(position) == CINEMA) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;

            RecyclerView recyclerView = viewHolder.title_recyclerview;
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            List<Movie> dataset = new ArrayList<Movie>();
            final HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
            recyclerView.setAdapter(adapter);

            EnableAutoScroll(recyclerView, layoutManager, adapter, 2);

            viewHolder.item_textview.setText(dataList.get(position).getTitolo());
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                final JSONArray movie_array = response.getJSONArray("results");
                                for (int index = 0; index < movie_array.length(); index++) {
                                    int movieid = movie_array.getJSONObject(index).getInt("id");
                                    RequestQueue trailersQueue = Volley.newRequestQueue(context);
                                    String videosUrl = "https://api.themoviedb.org/3/movie/" + movieid + "/videos?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
                                    JsonObjectRequest trailerRequest = new JsonObjectRequest
                                            (Request.Method.GET, videosUrl, null, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        JSONArray array = response.getJSONArray("results");
                                                        if (array.length() > 0) {
                                                            for (int index = 0; index < response.length(); index++) {
                                                                if (array.getJSONObject(index).getString("type").equals("Trailer")) {
                                                                    String youtubekey = array.getJSONObject(index).getString("key");
                                                                    String title = array.getJSONObject(index).getString("name");
                                                                    Movie temp = new Movie();
                                                                    temp.setYoutubekey(youtubekey);
                                                                    temp.setTitle(title);
                                                                    temp.setTipo("trailer");
                                                                    adapter.add(adapter.getItemCount(), temp);
                                                                    adapter.notifyDataSetChanged();
                                                                    break;
                                                                }
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
                                    trailersQueue.add(trailerRequest);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }

        if (getItemViewType(position) == POPOLARI) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.title_recyclerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                itemsRecyclerView = setMaxElemntsNumber(movie_array.length());
                                for (int index = 0; index < itemsRecyclerView; index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "recenti"));
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }

        if (getItemViewType(position) == ARRIVO) {

            final ViewHolderNoTitle viewHolder1 = (ViewHolderNoTitle) holder;
            viewHolder1.imageView.setBackgroundResource(R.drawable.upcoming_bg);
            viewHolder1.item_textview.setText(dataList.get(position).getTitolo());
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder1.without_title_recyclerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                itemsRecyclerView = setMaxElemntsNumber(movie_array.length());
                                for (int index = 0; index < itemsRecyclerView; index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "recenti"));
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }

        if (getItemViewType(position) == VOTATI) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.title_recyclerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                itemsRecyclerView = setMaxElemntsNumber(movie_array.length());
                                for (int index = 0; index < itemsRecyclerView; index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "recenti"));
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }

        if (getItemViewType(position) == CAST) {
            final ViewHolderNoCard viewHolder = (ViewHolderNoCard) holder;
            viewHolder.item_textview.setText(dataList.get(position).getTitolo());

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/person/popular?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.title_recyclerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                HorizontalAdapter adapter = new HorizontalAdapter(context);
                                recyclerView.setAdapter(adapter);
                                JSONArray cast_array = response.getJSONArray("results");
                                itemsRecyclerView = setMaxElemntsNumber(cast_array.length());
                                Log.i("TAG", String.valueOf(cast_array.length()));
                                for (int index = 0; index < itemsRecyclerView; index++) {
                                    Cast cast = new Cast();
                                    adapter.addCast(index, cast.parseCastJson(cast_array.getJSONObject(index), "cast"));
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            requestQueue.add(jsonObjectRequest1);
        }
    }

    private void EnableAutoScroll(final RecyclerView recyclerView, final LinearLayoutManager layoutManager, final HorizontalAdapter adapter, int speed) {
        final int duration = speed;
        final int pixelsToMove = speed;
        final Handler mHandler = new Handler(Looper.getMainLooper());
        final Runnable SCROLLING_RUNNABLE = new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollBy(pixelsToMove, 0);
                mHandler.postDelayed(this, duration);
            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                if (dx != pixelsToMove) {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                }
                if (lastItem == layoutManager.getItemCount() - 1) {
                    //mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(0);
                            //recyclerView.setAdapter(null);
                            //recyclerView.setAdapter(adapter);
                        }
                    }, 0);
                    mHandler.postDelayed(SCROLLING_RUNNABLE, 0);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 10000);
    }

    public void loadAdviceFilms(ViewHolderNoCard viewHolder, final List<Integer> listaFilmDaGuardare, final List<Integer> listaFilmGuardati) {
        RecyclerView recyclerView = viewHolder.title_recyclerview;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<Movie> dataset = new ArrayList<Movie>();
        final HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
        recyclerView.setAdapter(adapter);
        EnableAutoScroll(recyclerView, layoutManager, adapter, 2);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = "https://api.themoviedb.org/3/movie/" + listaFilmGuardati.get(0) + "/similar?api_key=" + API_KEY + "&language=" + LANG + "&page=1";
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie_array = response.getJSONArray("results");
                            System.out.println("guardati: " + listaFilmGuardati);
                            System.out.println("guardate: " + listaFilmDaGuardare);
                            int i = 0;
                            for (int index = 0; index < movie_array.length(); index++) {
                                Movie movie = new Movie();
                                if(!(listaFilmDaGuardare.contains(movie.parseAdviceMovieJson(movie_array.getJSONObject(index), "consigliati").getMovieId()) ||
                                        listaFilmGuardati.contains(movie.parseAdviceMovieJson(movie_array.getJSONObject(index), "consigliati").getMovieId()))){
                                    adapter.add(i, movie.parseAdviceMovieJson(movie_array.getJSONObject(index), "consigliati"));
                                    adapter.notifyDataSetChanged();
                                    i++;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", String.valueOf(error));
                    }
                });
        requestQueue.add(jsonObjectRequest1);
    }

    public int setMaxElemntsNumber(int length) {
        if (length > MAX_ITEMS_RECYCLERVIEW)
            return MAX_ITEMS_RECYCLERVIEW;
        else return length;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolderNoTitle extends RecyclerView.ViewHolder {
        private RecyclerView without_title_recyclerview;
        private ImageView imageView;
        private TextView item_textview;

        ViewHolderNoTitle(View view) {
            super(view);
            this.without_title_recyclerview = view.findViewById(R.id.without_title_recyclerview);
            this.imageView = view.findViewById(R.id.image_upcoming);
            this.item_textview = view.findViewById(R.id.item_textview);
        }
    }

    class ViewHolderNoCard extends RecyclerView.ViewHolder {
        private RecyclerView title_recyclerview;
        private TextView item_textview;
        private ConstraintLayout cointainer;
        ViewHolderNoCard(View view) {
            super(view);
            this.title_recyclerview = view.findViewById(R.id.title_recyclerview);
            this.item_textview = view.findViewById(R.id.item_textview);
            this.cointainer = view.findViewById(R.id.container);
        }
    }
}