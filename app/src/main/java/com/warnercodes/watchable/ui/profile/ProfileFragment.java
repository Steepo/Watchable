package com.warnercodes.watchable.ui.profile;

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
import com.warnercodes.watchable.adapter.ProfileAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemType> itemList;
    private int movieId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        itemList = new ArrayList<ItemType>();

        //First recyclerView
        recyclerView = view.findViewById(R.id.recyclerview_profile);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        itemList.add(new ItemType("", 1));
        itemList.add(new ItemType(getString(R.string.watched), 2));
        //itemList.add(new ItemType(getString(R.string.watchlist), 3));

        profileAdapter = new ProfileAdapter(itemList);
        recyclerView.setAdapter(profileAdapter);
    }
}
