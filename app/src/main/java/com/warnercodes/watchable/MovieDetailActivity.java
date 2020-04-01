package com.warnercodes.watchable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieDetailAdapter movieDetailAdapter;
    private List<Movie> mDataset;
    private RecyclerView.LayoutManager mLayoutManager;
    private int movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        movieId = intent.getIntExtra("movieId", 0);
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView) findViewById(R.id.details_recyclerview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        if (movieId == 0)
            requestMovieInfo(181812); //Star Wars The Rise of the Skywalker
        else
            requestMovieInfo(movieId);
    }

    public void requestMovieInfo(final int movieId){
        String url = "https://api.themoviedb.org/3/movie/"+movieId+"?api_key=db18c03be648dd161624fabd8596021a&language=en-US";
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Movie movie = new Movie();
                        movie.parseJson(response);
                        mDataset = movie.getMovie();
                        getSupportActionBar().setTitle(mDataset.get(0).getTitle());
                        Log.i("DEBUG", ""+mDataset.size());
                        movieDetailAdapter = new MovieDetailAdapter(mDataset);
                        recyclerView.setAdapter(movieDetailAdapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MovieDetailActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", String.valueOf(error));
                    }
                });

        requestQueue.add(jsonObjectRequest);
        Log.i("DEBUG", "END");
    }



}
