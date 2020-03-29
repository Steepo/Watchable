package com.warnercodes.watchable.ui.attivita;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.MainAdapter;
import com.warnercodes.watchable.R;


import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;
    private int movieId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        itemList = new ArrayList<ItemType>();

        //First recyclerView
        recyclerView = root.findViewById(R.id.main_recylerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        itemList.add(new ItemType("Attività Recenti", 1));
        itemList.add(new ItemType("Ti consigliamo anche", 2));
        itemList.add(new ItemType("Altri simili", 3));
        mainAdapter = new MainAdapter(itemList);
        recyclerView.setAdapter(mainAdapter);
        /*
        //Second recyclerView
        recyclerView1 = root.findViewById(R.id.rv_advice);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
/*
        //Third recyclerView
        recyclerView2 = root.findViewById(R.id.rv_similar);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
*/

        //movieId = 299;
        //final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //JsonRequest(requestQueue);

        return root;
    }
}
    /*
    private void JsonRequest(RequestQueue requestQueue) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=db18c03be648dd161624fabd8596021a&language=en-US";
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Secondo inizio");
                        Movie movie = new Movie();
                        movie.parseJson(response, "consigliati");
                        myDataset1 = movie.getMovies();
                        Log.i("DEBUG", "" + myDataset1.size());
                        recyclerViewActivityAdapter1 = new MainAdapter(myDataset1);
                        recyclerView1.setAdapter(mainAdapter);
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


    String popularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=db18c03be648dd161624fabd8596021a&language=en-US&page=1";
    JsonObjectRequest jsonPopular = new JsonObjectRequest
            (Request.Method.GET, popularUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray movie_array = null;
                    try {
                        movie_array = response.getJSONArray("results");
                        Log.i("TAG", String.valueOf(movie_array.length()));
                        System.out.println("Primo inizio");
                        for (int index = 0; index < movie_array.length(); index++) {
                            Movie movie = new Movie();
                            mainAdapter = new MainAdapter(myDataset);
                            mainAdapter.add(index, movie.parseSingleFilmJson(movie_array.getJSONObject(index), "recenti"));
                            mainAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mainAdapter);
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
        requestQueue.add(jsonObjectRequest1);
        requestQueue.add(jsonPopular);
    }
}

*/