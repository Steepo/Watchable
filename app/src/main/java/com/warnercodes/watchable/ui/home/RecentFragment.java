package com.warnercodes.watchable.ui.home;

import android.content.Context;
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

import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.R;
import com.warnercodes.watchable.adapter.AttivitaAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecentFragment extends Fragment {

    private RecentViewModel recentViewModel;
    private RecyclerView recyclerView;
    private AttivitaAdapter attivitaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recentViewModel =
                ViewModelProviders.of(this).get(RecentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activity, container, false);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        itemList = new ArrayList<ItemType>();

        //First recyclerView
        recyclerView = view.findViewById(R.id.main_recylerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        attivitaAdapter = new AttivitaAdapter(getContext(), itemList);
        itemList.add(new ItemType(getString(R.string.watchlist), 1));
        itemList.add(new ItemType(getString(R.string.suggested_title), 2));
        recyclerView.setAdapter(attivitaAdapter);
    }

    @Override
    public void onStart() {
        Log.i("CYCLE", "START");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("CYCLE", "RESUME");
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("CYCLE", "ATTACH");
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        Log.i("CYCLE", "ATT Fragment");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onPause() {
        Log.i("CYCLE", "PAUSE");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("CYCLE", "STOP");
        super.onStop();
    }
}
