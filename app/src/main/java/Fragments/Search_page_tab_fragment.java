package Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapters.Dashboard_Notifications_adapter;
import Models.Compact_Project_Object;
import RetrofitModels.Bid_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_page_tab_fragment extends Fragment {


    View view;
    protected Boolean mVisibleToUserFlag = null;
    public RecyclerView myrecyclerview;
    public List<Compact_Project_Object> list_of_projects =new ArrayList<>();
    public  static ProgressBar bar;
    public Dashboard_Notifications_adapter recyclerAdapter;

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
        recyclerAdapter =new Dashboard_Notifications_adapter(getContext(),list_of_projects);
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


    class update_each_project_object extends Thread{

        @Override
        public void run() {
            super.run();
                final Calendar cal = Calendar.getInstance();
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                final long[] difference = new long[1];
                difference[0]=Long.MAX_VALUE;
                for (int i= 0 ; i< list_of_projects.size(); i++) {
                    Call<List<Bid_Object>> call0 = RetrofitClient.getInstance().getApi().getAllBidsOfProject("token " + LoginActivity.getCREDENTIALS()[0], list_of_projects.get(i).getId());
                    final int finalI = i;
                    call0.enqueue(new Callback<List<Bid_Object>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Bid_Object>> call0, @NonNull Response<List<Bid_Object>> response3) {
                            final List<Bid_Object> editresponse = response3.body();
                            for (int j = 0; j < editresponse.size(); j++) {
                                Date currentdate = cal.getTime();
                                Date creation = new Date();
                                try {
                                    creation = format.parse(editresponse.get(j).getCreatedAt());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (creation.getTime() - currentdate.getTime() < difference[0]) {
                                    difference[0] = creation.getTime() - currentdate.getTime();
                                }
                            }


                            long days = ((difference[0] / (1000 * 60 * 60 * 24)));
                            long hours = (((difference[0] - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)));
                            long minutes = ((difference[0] - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60));
                            start();
                            String time = "";
                            if (editresponse.size() > 0) {
                                if (days > 0) {
                                    time += days + " days ";
                                }
                                if (hours > 0) {
                                    time += hours + " hours ";
                                }
                                if (minutes > 0) {
                                    time += minutes + " minutes ago ";
                                }
                                final String finalTime = time;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        list_of_projects.get(finalI).setNumberofbidsandlastupdate(editresponse.size() + " bids - " + finalTime);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<List<Bid_Object>> call0, @NonNull Throwable t) {}
                    });
                }
        }

    }

}

