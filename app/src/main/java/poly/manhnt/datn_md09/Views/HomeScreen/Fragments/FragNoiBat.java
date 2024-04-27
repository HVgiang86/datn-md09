package poly.manhnt.datn_md09.Views.HomeScreen.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import poly.manhnt.datn_md09.Adapters.NoiBatAdapter;
import poly.manhnt.datn_md09.R;

public class FragNoiBat extends Fragment {
    RecyclerView recyclerView;
    NoiBatAdapter noiBatAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noibat, container, false);
//        recyclerView = view.findViewById(R.id.recyclerNoiBat);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        noiBatAdapter = new NoiBatAdapter(getActivity(), list);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(noiBatAdapter);
//        noiBatAdapter.notifyDataSetChanged();
        return view;
    }
}
