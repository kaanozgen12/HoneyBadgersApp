package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import Adapters.ActivityAdapterVerifiedProject;
import Models.Compact_Project_Object;
import honeybadgersapp.honeybadgers.R;

public class ProjectProgressFragment extends Fragment {


    View view;
    SeekBar seekBar;
    public List<Compact_Project_Object> list_of_activities =new ArrayList<>();
    RecyclerView mRecylerView;
    ActivityAdapterVerifiedProject recyclerAdapter;

    public ProjectProgressFragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.project_progress_fragment,container,false);
        seekBar= view.findViewById(R.id.project_progress_seekBar);
        mRecylerView = view.findViewById(R.id.project_progress_recyclerview);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    @Override
    public void onResume() {
        super.onResume();


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
