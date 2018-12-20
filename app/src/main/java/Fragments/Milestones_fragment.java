package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Milestones_adapter;
import RetrofitModels.Milestone_Object;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;

public class Milestones_fragment extends Fragment {


    View view;
    public Timer timer= new Timer();
    protected Boolean mVisibleToUserFlag = null;
    public Button create_project;
    private TextView requested_text;
    private TextView verified_text;
    public RecyclerView myrecyclerview_proposed;
    public RecyclerView myrecyclerview_verified;
    public List<Milestone_Object> proposed_milestones =new ArrayList<>();
    public List<Milestone_Object> verified_milestones =new ArrayList<>();
    public Milestones_adapter recyclerAdapter_proposed;
    public Milestones_adapter recyclerAdapter_verified;

    public Milestones_fragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.milestones_fragment,container,false);
        verified_text= view.findViewById(R.id.milestones_fragment_requested_no_verified_available);
        requested_text= view.findViewById(R.id.milestones_fragment_requested_no_requested_available);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        create_project= view.findViewById(R.id.milestones_fragment_create_milestone_button);
        myrecyclerview_proposed =  view.findViewById(R.id.milestones_fragment_requested_milestones_recyclerview);
        recyclerAdapter_proposed =new Milestones_adapter(getContext(),proposed_milestones);
        myrecyclerview_proposed.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview_proposed.setAdapter(recyclerAdapter_proposed);

        myrecyclerview_verified =  view.findViewById(R.id.milestones_fragment_milestones_recyclerview);
        recyclerAdapter_verified =new Milestones_adapter(getContext(),verified_milestones);
        myrecyclerview_verified.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview_verified.setAdapter(recyclerAdapter_verified);
        addTimerActivity();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.purge();
        timer.cancel();
        timer=null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && (mVisibleToUserFlag == null || mVisibleToUserFlag == false)) {
            mVisibleToUserFlag = true;
        } else if (!isVisibleToUser && mVisibleToUserFlag == null) {
            mVisibleToUserFlag = false;
        } else if (!isVisibleToUser && mVisibleToUserFlag) {
            mVisibleToUserFlag = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVisibleToUserFlag = null;
    }

    public void addTimerActivity() throws NullPointerException{

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(LoginActivity.getCREDENTIALS()[3]!=null && LoginActivity.getCREDENTIALS()[3].equals("Client")){
                            create_project.setText("  Create Milestone  ");
                        }else if(LoginActivity.getCREDENTIALS()[3]!=null && LoginActivity.getCREDENTIALS()[3].equals("Freelancer")){
                            create_project.setText("  Propose Milestone  ");
                        }
                        if (proposed_milestones.size()==0){
                            requested_text.setVisibility(View.VISIBLE);
                        }else{
                            requested_text.setVisibility(View.GONE);
                        }
                        if (verified_milestones.size()==0){
                            verified_text.setVisibility(View.VISIBLE);
                        }else{
                            verified_text.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 0, 1000);
    }




}


