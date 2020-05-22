package com.warnercodes.watchable.ui.search;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.warnercodes.watchable.Movie;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.adapter.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.warnercodes.watchable.Costants.API_KEY;
import static com.warnercodes.watchable.Costants.LANG;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchAdapter searchAdapter;
    private List<Movie> mDataset;
    private RecyclerView recyclerView;
    private ImageView searchImageview;
    private TextView noMoviesText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_search, container, false);
        ///TODO: implement bindings
        TextInputEditText editText = root.findViewById(R.id.textinputedittext);

        recyclerView = root.findViewById(R.id.results_recyclerview);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mDataset = new ArrayList<Movie>();
        searchAdapter = new SearchAdapter(mDataset);
        recyclerView.setAdapter(searchAdapter);
        searchImageview = root.findViewById(R.id.search_imageview);
        noMoviesText = root.findViewById(R.id.textview_no_movies);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() < 3) {
                    mDataset.removeAll(mDataset);
                    searchAdapter.notifyDataSetChanged();
                    searchImageview.setVisibility(View.VISIBLE);
                    noMoviesText.setVisibility(View.VISIBLE);
                } else {
                    String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=" + LANG + "&query=" + text.replace(" ", "%20") + "&page=1&include_adult=false";
                    searchImageview.setVisibility(View.INVISIBLE);
                    noMoviesText.setVisibility(View.INVISIBLE);
                    final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONArray movie_array = null;
                                    try {
                                        movie_array = response.getJSONArray("results");
                                        for (int index = 0; index < movie_array.length(); index++) {
                                            Movie movie = new Movie();
                                            searchAdapter.add(index, movie.parseSingleMovieJson(movie_array.getJSONObject(index), ""));
                                            searchAdapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                    requestQueue.add(jsonObjectRequest);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (Math.abs(scrollY + oldScrollY) > 0) {
                        hideKeyboard(getActivity());
                    }
                }
            });
        }

        return root;
    }

    private void hideKeyboard(FragmentActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
