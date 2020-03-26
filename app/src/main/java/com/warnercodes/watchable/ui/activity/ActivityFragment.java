package com.warnercodes.watchable.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.R;


import java.util.ArrayList;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Movie> myDataset = new ArrayList<Movie>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        popoluteDataset();
        mAdapter = new RecyclerViewActivityAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }
    private void popoluteDataset(){
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
        myDataset.add(new Movie(R.drawable.shaw));
    }
}
