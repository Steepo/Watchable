package com.warnercodes.watchable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieDetailAdapter movieDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


    }




}
