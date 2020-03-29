package com.warnercodes.watchable;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttivitaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemType> dataList;
    public int typeReq;
    Context mContex;

    public AttivitaAdapter(List<ItemType> dataList) {
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

    private static int RECENTI = 1;
    private static int COSIGLIATI = 2;
    private static int SIMILI = 3;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_recylerview, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_recylerview, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == 3) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_recylerview, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == RECENTI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Attività Recenti");
            final Context context = viewHolder.item_recylerview.getContext();

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
                            System.out.println("Secondo inizio");
                            HorizontalAdapter adapter = new HorizontalAdapter(dataset);
                            recyclerView.setAdapter(adapter);
                            JSONArray  movie_array = response.getJSONArray("results");
                            Log.i("TAG", String.valueOf(movie_array.length()));
                            for (int index = 0; index < movie_array.length(); index++) {
                                Movie movie = new Movie();
                                adapter.add(index, movie.parseSingleFilmJson(movie_array.getJSONObject(index), "recenti"));
                                Log.i("DATASET", dataset.get(0).toString());
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
            System.out.println("Secondo fatto");
            requestQueue.add(jsonObjectRequest1);

        }
        if (getItemViewType(position) == COSIGLIATI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Ti Cons");
        }
        if (getItemViewType(position) == SIMILI) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.item_textview.setText("Simili");
        }
    }

    @Override
    public int getItemViewType(int position) {
        int tipo = dataList.get(position).getType();
        if (tipo == 1)
            return RECENTI;
        if (tipo == 2)
            return COSIGLIATI;
        if (tipo == 3)
            return SIMILI;
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
}
