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
    private RecyclerViewActivityAdapter recyclerViewActivityAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> myDataset;
    private int movieId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        //First recyclerView
        recyclerView = root.findViewById(R.id.rv_recent_act);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        myDataset = new ArrayList<Movie>();

        //Second recyclerView
        recyclerView1 = root.findViewById(R.id.rv_advice);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
        movieId = 210;

        String url = "https://api.themoviedb.org/3/movie/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray movie_array = null;

                        try {
                            movie_array = response.getJSONArray("results");
                            Log.i("TAG", String.valueOf(movie_array.length()));
                            for (int index = 0; index < movie_array.length(); index++) {
                                Movie movie = new Movie();
                                recyclerViewActivityAdapter = new RecyclerViewActivityAdapter(myDataset,2);
                                recyclerViewActivityAdapter.add(index, movie.parseSearchJson(movie_array.getJSONObject(index)));
                                recyclerViewActivityAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(recyclerViewActivityAdapter);
                            }
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
        requestQueue.add(jsonObjectRequest);

        url = "https://api.themoviedb.org/3/movie/5123?api_key=db18c03be648dd161624fabd8596021a&language=en-US";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                            Movie movie = new Movie();
                            movie.parseJson(response);
                            myDataset = movie.getMovies();
                            Log.i("DEBUG", ""+myDataset.size());
                            recyclerViewActivityAdapter = new RecyclerViewActivityAdapter(myDataset,1);
                            recyclerView1.setAdapter(recyclerViewActivityAdapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", String.valueOf(error));
                    }
                });

        requestQueue.add(jsonObjectRequest1);
        Log.i("DEBUG", "END");
        return root;
    }
}
