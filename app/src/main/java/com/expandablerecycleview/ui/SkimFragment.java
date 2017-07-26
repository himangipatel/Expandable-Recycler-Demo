package com.expandablerecycleview.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expandablerecycleview.PeopleAdapter;
import com.expandablerecycleview.R;
import com.expandablerecycleview.customview.ExpandableRecyclerAdapter;

/**
 * Created by Himangi on 14/7/17
 */

public class SkimFragment extends Fragment {

    RecyclerView recycler;
    PeopleAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skim,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = (RecyclerView) view.findViewById(R.id.main_recycler);

        adapter = new PeopleAdapter(getActivity());
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);

    }
}
