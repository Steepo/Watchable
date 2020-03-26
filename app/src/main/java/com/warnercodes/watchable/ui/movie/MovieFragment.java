package com.warnercodes.watchable.ui.movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.warnercodes.watchable.R;

public class MovieFragment extends Fragment{

    private MovieViewModel movieViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie, container, false);
        return root;
    }
}
