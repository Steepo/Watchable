package com.warnercodes.watchable.ui.recent;

import android.os.Bundle;
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
        itemList = new ArrayList<ItemType>();

        //First recyclerView
        recyclerView = root.findViewById(R.id.main_recylerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        attivitaAdapter = new AttivitaAdapter(getActivity(), itemList);
        itemList.add(new ItemType(getString(R.string.recent_activities_title), 1));
        itemList.add(new ItemType(getString(R.string.suggested_title), 2));
        recyclerView.setAdapter(attivitaAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
