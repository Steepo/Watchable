package com.warnercodes.watchable.ui.movie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.adapter.AttivitaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {

    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private AttivitaAdapter attivitaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie, container, false);
        itemList = new ArrayList<ItemType>();
        Log.i("adapter", "1");
        recyclerView = root.findViewById(R.id.main_recylerview_movie);
        Log.i("adapter", "2");
        recyclerView.setHasFixedSize(true);
        Log.i("adapter", "3");
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Log.i("adapter", "4");
        recyclerView.setLayoutManager(layoutManager);
        itemList.add(new ItemType("I più popolari", 5));
        itemList.add(new ItemType("In arrivo", 6));
        itemList.add(new ItemType("Gli attori del momento", 8));
        itemList.add(new ItemType("Adesso al cinema", 4));
        itemList.add(new ItemType("I più votati", 7));
        Log.i("adapter", "5");
        attivitaAdapter = new AttivitaAdapter(getActivity(), itemList);
        Log.i("adapter", "6");
        recyclerView.setAdapter(attivitaAdapter);
        Log.i("adapter", "7");
        return root;
    }
}
