package com.warnercodes.watchable.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.MovieSearchAdapter;
import com.warnercodes.watchable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieSearchAdapter movieSearchAdapter;
    private List<Movie> mDataset;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        TextInputLayout textfield = root.findViewById(R.id.textfield);

        TextInputEditText editText = root.findViewById(R.id.textinputedittext);

        recyclerView = root.findViewById(R.id.results_recyclerview);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mDataset = new ArrayList<Movie>();
        movieSearchAdapter = new MovieSearchAdapter(mDataset);
        recyclerView.setAdapter(movieSearchAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mDataset = new ArrayList<Movie>();

                String text = (String) s.toString();
                String url = "https://api.themoviedb.org/3/search/movie?api_key=db18c03be648dd161624fabd8596021a&language=en-US&query=" + text.replace(" ", "%20") + "&page=1&include_adult=false";
                if (text.length()==0)
                    mDataset.removeAll(mDataset);

                else {
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
                                            movieSearchAdapter.add(index, movie.parseSearchJson(movie_array.getJSONObject(index)));
                                            Log.i("DATASET", mDataset.get(0).toString());
                                            movieSearchAdapter.notifyDataSetChanged();
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return root;
    }
}
