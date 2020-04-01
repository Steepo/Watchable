package com.warnercodes.watchable.ui.attivita;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warnercodes.watchable.ItemType;
import com.warnercodes.watchable.AttivitaAdapter;
import com.warnercodes.watchable.R;


import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private RecyclerView recyclerView;
    private AttivitaAdapter attivitaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;

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
        itemList.add(new ItemType("Attività recenti", 1));
        itemList.add(new ItemType("Ti consigliamo anche", 2));
        itemList.add(new ItemType("Altri simili", 3));
        attivitaAdapter = new AttivitaAdapter(itemList);
        recyclerView.setAdapter(attivitaAdapter);

        return root;
    }

}
