package Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Dashboard_Notifications_adapter;
import Models.Compact_Project_Object;
import RetrofitModels.Bid_Object;
import RetrofitModels.Recommended_Project_Object;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recommended_Search_Fragment extends Fragment {


    View view;
    protected Boolean mVisibleToUserFlag = null;
    public RecyclerView myrecyclerview;
    public List<Compact_Project_Object> list_of_projects =new ArrayList<>();
    public  static ProgressBar bar;
    public Dashboard_Notifications_adapter recyclerAdapter;
    public Timer timer= new Timer();

    public Recommended_Search_Fragment() {

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
        fill_recommended();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        myrecyclerview.setAdapter(null);
        try {
            recyclerAdapter.getTimer().cancel();
            recyclerAdapter.getTimer().purge();
        }catch (NullPointerException e){}

    }

    public  void addObject(Compact_Project_Object a){
        list_of_projects.add(a);
    }
    /**
     * This method invoked when page stand visible to user.
     */
    public  void onVisible(){recyclerAdapter.notifyDataSetChanged();};

    public  void fill_recommended() throws NullPointerException{
        Call<List<Recommended_Project_Object>> call = RetrofitClient.getInstance().getApi().getRecommendedProjects("token "+LoginActivity.getCREDENTIALS()[0]);
        call.enqueue(new Callback<List<Recommended_Project_Object>>() {
            @Override
            public void onResponse(Call<List<Recommended_Project_Object>> call, Response<List<Recommended_Project_Object>> response) {
                List<Recommended_Project_Object> editResponse = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < editResponse.size(); i++) {

                            final ArrayList<Tag_Object> t = new ArrayList<>();
                            Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(), editResponse.get(i).getTitle(), editResponse.get(i).getBudgetMin() + "-" + editResponse.get(i).getBudgetMax(), "0", t, false, false);

                            for (int j = 0; j < editResponse.get(i).getTags().length; j++) {
                                Call<Tag_Object> call2 = RetrofitClient.getInstance().getApi().getTag(editResponse.get(i).getTags()[j]);
                                call2.enqueue(new Callback<Tag_Object>() {
                                    @Override
                                    public void onResponse(Call<Tag_Object> call2, Response<Tag_Object> response) {
                                        Tag_Object editResponse = response.body();
                                        if (response.isSuccessful()) {
                                            t.add(editResponse);
                                            recyclerAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Tag_Object> call2, Throwable t) {
                                        Log.d("MyTag","FAILED RECOMMENDED PROJECTS_!_!__!_!_!!_!__!");
                                    }
                                });

                            }
                            list_of_projects.add(temp);
                            recyclerAdapter.notifyItemInserted(recyclerAdapter.getItemCount() - 1);
                    }

                }else {
                    Log.d("MyTag","FAILED RECOMMENDED RESPONCE UNSUCESS PROJECTS_!_!__!_!_!!_!__!");
                }

                addTimerActivity(list_of_projects , myrecyclerview);
            }
            @Override
            public void onFailure(Call<List<Recommended_Project_Object>> call, Throwable t) {
                Log.d("MyTag","FAILED RECOMMENDED PROJECTS_!_!__!_!_!!_!__!");
            }
        });

    }

    public void addTimerActivity(final List<Compact_Project_Object> list_of_projects, final RecyclerView recycler) throws NullPointerException{

        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recycler.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (firstVisiblePosition<0)
                    firstVisiblePosition=0;
                if (lastVisiblePosition<0)
                    lastVisiblePosition=0;
                if (lastVisiblePosition>=list_of_projects.size())
                    lastVisiblePosition=list_of_projects.size();
                // Log.d("MyTag","::::::firstvisible::::: "+firstVisiblePosition+"last::::: "+lastVisiblePosition);

                if (list_of_projects.size()!=0){
                    for (int i= firstVisiblePosition; i<=lastVisiblePosition; i++){
                        final Calendar cal = Calendar.getInstance();
                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                        final long[] difference = new long[1];
                        Call<List<Bid_Object>> call0 = RetrofitClient.getInstance().getApi().getAllBidsOfProject("token " + LoginActivity.getCREDENTIALS()[0],list_of_projects.get(i).getId());
                        final int finalI = i;
                        call0.enqueue(new Callback<List<Bid_Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<Bid_Object>> call0, @NonNull Response<List<Bid_Object>> response3) {
                                final List<Bid_Object> editresponse = response3.body();
                                for (int j = 0; j < editresponse.size(); j++) {
                                    Date currentdate = cal.getTime();
                                    Date creation = null;
                                    try {
                                        creation = format.parse(editresponse.get(j).getUpdatedAt());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (difference[0] == 0) {
                                        difference[0] = currentdate.getTime() - creation.getTime() - 10730000 ;
                                    } else if (currentdate.getTime() - creation.getTime() -10730000 < difference[0]) {
                                        difference[0] = currentdate.getTime() - creation.getTime() - 10730000;
                                    }
                                }
                                long days = ((difference[0] / (1000 * 60 * 60 * 24)));
                                long hours = (((difference[0] - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)));
                                long minutes = ((difference[0] - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60));
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
                                }
                                if (list_of_projects.get(finalI).isMoneySignFirst() && ((recycler.findViewHolderForAdapterPosition(finalI))!=null)&&((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign()!=null &&editresponse.size()>Integer.parseInt(list_of_projects.get(finalI).getNumberofbidsandlastupdate().substring(0,list_of_projects.get(finalI).getNumberofbidsandlastupdate().indexOf(" "))))
                                { Log.d("MyTag","----------------------------------------");
                                    final String finalTime = time;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setAlpha(0f);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setVisibility(View.VISIBLE);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().animate()
                                                        .alpha(1f)
                                                        .setDuration(600)
                                                        .setListener(null);

                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setAlpha(1f);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().animate()
                                                        .alpha(0f)
                                                        .setDuration(600)
                                                        .setListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setVisibility(View.GONE);
                                                                list_of_projects.get(finalI).setNumberofbidsandlastupdate(String.format("%d bids - %s", editresponse.size(), finalTime));

                                                            }
                                                        });
                                            }catch (NullPointerException e){
                                                Log.d("MyTag","null pointer dereferencing caught");
                                            }
                                        }
                                    });
                                }
                                list_of_projects.get(finalI).setMoneySignFirst(true);
                               /* if(!stable[0]&&(recycler.findViewHolderForAdapterPosition(finalI))!=null &&editresponse.size()==Integer.parseInt(list_of_projects.get(finalI).getNumberofbidsandlastupdate().substring(0,list_of_projects.get(finalI).getNumberofbidsandlastupdate().indexOf(" ")))){
                                   // Log.d("MyTag","----------------------------------------1");
                                    stable[0] =true;
                                    ((Dashboard_Notifications_adapter.MyViewHolder)recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().setVisibility(View.VISIBLE);

                                }else{
                                   // Log.d("MyTag","----------------------------------------2");
                                    stable[0] =false;
                                }*/
                                try {
                                    if ((recycler.findViewHolderForAdapterPosition(finalI)) != null && !((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().getText().toString().equals(String.format("%d bids - %s", editresponse.size(), time))) {
                                        ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().setText(String.format("%d bids - %s", editresponse.size(), time));
                                        list_of_projects.get(finalI).setNumberofbidsandlastupdate(String.format("%d bids - %s", editresponse.size(), time));
                                    }
                                }catch (NullPointerException e){
                                    Log.d("MyTag","null pointer dereferencing caught" );
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<List<Bid_Object>> call0, @NonNull Throwable t) {}
                        });
                    }
                }
            }
        }, 0, 1000);
    }

}

