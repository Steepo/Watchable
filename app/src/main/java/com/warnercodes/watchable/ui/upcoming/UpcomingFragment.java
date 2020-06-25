package com.warnercodes.watchable.ui.upcoming;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.adapter.AttivitaAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment {

    private UpcomingViewModel upcomingViewModel;
    private RecyclerView recyclerView;
    private AttivitaAdapter attivitaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;
    private RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        upcomingViewModel = ViewModelProviders.of(this).get(UpcomingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        itemList = new ArrayList<ItemType>();
        recyclerView = view.findViewById(R.id.main_recylerview_movie);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        itemList.add(new ItemType(getString(R.string.upcoming_title), 6));
        itemList.add(new ItemType(getString(R.string.now_playing_title), 4));
        itemList.add(new ItemType(getString(R.string.popular_title), 5));
        itemList.add(new ItemType(getString(R.string.top_rated_title), 7));
        itemList.add(new ItemType(getString(R.string.cast_title), 8));
        Log.i("adapter", "5");
        requestQueue = Volley.newRequestQueue(getActivity());
        attivitaAdapter = new AttivitaAdapter(getContext(), itemList, requestQueue);
        Log.i("adapter", "6");
        recyclerView.setAdapter(attivitaAdapter);
        Log.i("adapter", "7");
    }
}
