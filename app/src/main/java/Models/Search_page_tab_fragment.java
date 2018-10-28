package Models;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import Adapters.RecyclerViewDashboard_Notifications;
import honeybadgersapp.honeybadgers.R;

public class Search_page_tab_fragment extends Fragment {


    View view;
    protected Boolean mVisibleToUserFlag = null;
    public RecyclerView myrecyclerview;
    public List<Compact_Project_Object> list_of_projects =new ArrayList<>();
    public  static ProgressBar bar;
    public RecyclerViewDashboard_Notifications recyclerAdapter;

    public Search_page_tab_fragment() {

    }



    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public RecyclerView getMyrecyclerview() {
        return myrecyclerview;
    }

    public List<Compact_Project_Object> getList_of_projects() {
        return list_of_projects;
    }

    public ProgressBar getBar() {
        return bar;
    }

    public void setBar(ProgressBar bar) {
        this.bar = bar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.search_page_fragment,container,false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myrecyclerview =  view.findViewById(R.id.tab_recyclerview);
        recyclerAdapter =new RecyclerViewDashboard_Notifications(getContext(),list_of_projects);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
        bar =  view.findViewById(R.id.search_page_tab_fragment_progressbar);
    }
    @Override
    public void onResume() {
        super.onResume();

        if (mVisibleToUserFlag != null && mVisibleToUserFlag) {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && (mVisibleToUserFlag == null || mVisibleToUserFlag == false)) {
            mVisibleToUserFlag = true;
            if (isResumed()) {
                onVisible();
            }
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

    public  void addObject(Compact_Project_Object a){
        list_of_projects.add(a);
    }
    /**
     * This method invoked when page stand visible to user.
     */
    public  void onVisible(){recyclerAdapter.notifyDataSetChanged();};



    public  void addProjects(){

    }

}

