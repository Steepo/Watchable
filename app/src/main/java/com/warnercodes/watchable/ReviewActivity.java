package com.warnercodes.watchable;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.adapter.HorizontalAdapter;
import com.warnercodes.watchable.databinding.ActivityReviewsBinding;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewsBinding binding;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityReviewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Review review = (Review) intent.getSerializableExtra("review");
        List<Review> reviewList = new ArrayList<Review>();
        reviewList = (List<Review>) intent.getSerializableExtra("reviews");
        //Log.i("PARCELABEL", review.toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User Reviews");


        recyclerView = binding.reviewsRecyclerview;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        HorizontalAdapter movieDetailAdapter = new HorizontalAdapter(this);
        if (review != null)
            movieDetailAdapter.setCompleteReview(review);
        else
            movieDetailAdapter.setReviewList(reviewList);

        recyclerView.setAdapter(movieDetailAdapter);
        movieDetailAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
