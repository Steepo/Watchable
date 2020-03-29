package com.warnercodes.watchable.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.warnercodes.watchable.ui.activity.RecyclerViewActivityAdapter;
import com.warnercodes.watchable.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerViewActivityAdapter recyclerViewActivityAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> myDataset;
    private int movieId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        myDataset = new ArrayList<Movie>();

        //First recyclerView
        recyclerView = root.findViewById(R.id.rv_recent_act);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Second recyclerView
        recyclerView1 = root.findViewById(R.id.rv_advice);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);

        //Third recyclerView
        recyclerView2 = root.findViewById(R.id.rv_similar);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);

        movieId = 299;

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = "https://api.themoviedb.org/3/movie/"+movieId+"?api_key=db18c03be648dd161624fabd8596021a&language=en-US";
        JsonRequest(url, 1, requestQueue);
        url = "https://api.themoviedb.org/3/movie/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
        JsonRequest(url, 2, requestQueue);

        return root;
    }
    private void JsonRequest(String url, int typeReq, RequestQueue requestQueue ){
        if(typeReq == 1) {
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Secondo inizio");
                            Movie movie = new Movie();
                            movie.parseJson(response);
                            myDataset = movie.getMovies();
                            Log.i("DEBUG", "" + myDataset.size());
                            recyclerViewActivityAdapter = new RecyclerViewActivityAdapter(myDataset, 1);
                            recyclerView1.setAdapter(recyclerViewActivityAdapter);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", String.valueOf(error));
                        }
                    });
            System.out.println("Secondo fatto");
            requestQueue.add(jsonObjectRequest1);
        }
        else{
            if (typeReq == 2){
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray movie_array = null;
                                try {
                                    movie_array = response.getJSONArray("results");
                                    Log.i("TAG", String.valueOf(movie_array.length()));
                                    System.out.println("Primo inizio");
                                    for (int index = 0; index < movie_array.length(); index++) {
                                        Movie movie = new Movie();
                                        recyclerViewActivityAdapter = new RecyclerViewActivityAdapter(myDataset,2);
                                        recyclerViewActivityAdapter.add(index, movie.parseSingleFilmJson(movie_array.getJSONObject(index)));
                                        recyclerViewActivityAdapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(recyclerViewActivityAdapter);
                                    }
                                    myDataset = new ArrayList<Movie>();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("DEBUG", String.valueOf(error));
                            }
                        });
                System.out.println("Primo fatto");
                requestQueue.add(jsonObjectRequest);
            }/*
            else{
                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray movie_array = null;
                                try {
                                    movie_array = response.getJSONArray("results");
                                    Log.i("TAG", String.valueOf(movie_array.length()));
                                    System.out.println("Terzo inizio");
                                    for (int index = 0; index < movie_array.length(); index++) {
                                        Movie movie = new Movie();
                                        recyclerViewActivityAdapter = new RecyclerViewActivityAdapter(myDataset,3);
                                        recyclerViewActivityAdapter.add(index, movie.parseSingleFilmJson(movie_array.getJSONObject(index)));
                                        recyclerViewActivityAdapter.notifyDataSetChanged();
                                        recyclerView2.setAdapter(recyclerViewActivityAdapter);
                                    }
                                    System.out.println("Terzo fatto");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("DEBUG", String.valueOf(error));
                            }
                        });
                requestQueue.add(jsonObjectRequest2);
            }*/
        }
    }
}
