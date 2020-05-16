package com.warnercodes.watchable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.warnercodes.watchable.Cast;
import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttivitaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> dataList;
    private Context context;

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
        if(viewType == 6){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_without_title, parent, false);
            return new ViewHolderNoTitle(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == RECENTI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Attività recenti");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                for (int index = 0; index < movie_array.length(); index++) {
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
        if (getItemViewType(position) == CONSIGLIATI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Ti consigliamo anche");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/201?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            RecyclerView recyclerView = viewHolder.item_recylerview;
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            List<Movie> dataset = new ArrayList<Movie>();
                            HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                            Movie movie = new Movie();
                            Log.i("GENERI", String.valueOf(response));
                            adapter.add(0, movie.parseSingleMovieJson(response, "consigliati"));
                            Log.i("DATASET", dataset.get(0).toString());
                            recyclerView.setAdapter(adapter);
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
        if (getItemViewType(position) == SIMILI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Altri simili");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/255/similar?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                Log.i("TAG", String.valueOf(movie_array.length()));
                                for (int index = 0; index < movie_array.length(); index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "simili"));
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
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Adesso al cinema");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                Log.i("TAG", String.valueOf(movie_array.length()));
                                for (int index = 0; index < movie_array.length(); index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "cinema"));
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

        if (getItemViewType(position) == POPOLARI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("I più popolari");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                Log.i("TAG", String.valueOf(movie_array.length()));
                                for (int index = 0; index < movie_array.length(); index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "popolari"));
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
            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
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
                                Log.i("TAG", String.valueOf(movie_array.length()));
                                for (int index = 0; index < movie_array.length(); index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "arrivo"));
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
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("I più votati");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                List<Movie> dataset = new ArrayList<Movie>();
                                HorizontalAdapter adapter = new HorizontalAdapter(context, dataset);
                                recyclerView.setAdapter(adapter);
                                JSONArray movie_array = response.getJSONArray("results");
                                Log.i("TAG", String.valueOf(movie_array.length()));
                                for (int index = 0; index < movie_array.length(); index++) {
                                    Movie movie = new Movie();
                                    adapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), "votati"));
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
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Gli attori del momento");

            final RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.themoviedb.org/3/person/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                RecyclerView recyclerView = viewHolder.item_recylerview;
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                HorizontalAdapter adapter = new HorizontalAdapter(context);
                                recyclerView.setAdapter(adapter);
                                JSONArray cast_array = response.getJSONArray("results");
                                Log.i("TAG", String.valueOf(cast_array.length()));
                                for (int index = 0; index < cast_array.length(); index++) {
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

    @Override
    public int getItemViewType(int position) {
        int tipo = dataList.get(position).getType();
        if (tipo == 1)
            return RECENTI;
        if (tipo == 2)
            return CONSIGLIATI;
        if (tipo == 3)
            return SIMILI;
        if (tipo == 4)
            return CINEMA;
        if (tipo == 5)
            return POPOLARI;
        if (tipo == 6)
            return ARRIVO;
        if (tipo == 7)
            return VOTATI;
        if (tipo == 8)
            return CAST;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_textview;
        private RecyclerView item_recylerview;
        //private Chip chip_more;

        ViewHolder(View view) {
            super(view);
            this.item_textview = view.findViewById(R.id.item_textview);
            this.item_recylerview = view.findViewById(R.id.title_recyclerview);
            //this.chip_more = view.findViewById(R.id.chip_more);
        }
    }

    class ViewHolderNoTitle extends RecyclerView.ViewHolder {
        private RecyclerView without_title_recyclerview;

        ViewHolderNoTitle(View view) {
            super(view);
            this.without_title_recyclerview = view.findViewById(R.id.without_title_recyclerview);
        }
    }
}
