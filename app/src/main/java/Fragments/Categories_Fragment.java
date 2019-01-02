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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Categories_Fragment extends Fragment {


    View view;
    public List<Compact_Project_Object> list_of_projects =new ArrayList<>();
    RecyclerView mRecylerView;
    int category=1;
    TextView primary_text;
    LinearLayout primary ;
    LinearLayout secondary;
    ImageView back_buttom;
    public Timer timer= new Timer();
    public  Dashboard_Notifications_adapter recyclerAdapter;

    public Categories_Fragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_category,container,false);
        mRecylerView = view.findViewById(R.id.project_categories_recyclerview);

        primary_text = view.findViewById(R.id.activity_category_primary_text);
        primary = view.findViewById(R.id.activity_category_primary_activity_layout);
        secondary = view.findViewById(R.id.activity_category_secondary_activity_layout);
        back_buttom = view.findViewById(R.id.project_categories_backbutton);
        primary_text.setVisibility(View.GONE);
        recyclerAdapter =new Dashboard_Notifications_adapter(getContext(),list_of_projects);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerView.setAdapter(recyclerAdapter);


        RelativeLayout relative1 = view.findViewById(R.id.button_with_image_above_text1);
        back_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category=1;primary.setVisibility(View.VISIBLE);secondary.setVisibility(View.GONE);
                if(timer!=null){
                    timer.purge();
                    timer.cancel();
                    timer=null;
                }
            }
        });
        relative1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=1;primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);
                fill_category(1);

            }
        });
        RelativeLayout relative2 = view.findViewById(R.id.button_with_image_above_text2);
        relative2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=2;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(2);
            }
        });
        RelativeLayout relative3 =  view.findViewById(R.id.button_with_image_above_text3);
        relative3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=3;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(3);
            }
        });
        RelativeLayout relative4 =  view.findViewById(R.id.button_with_image_above_text4);
        relative4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=4;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(4);
            }
        });
        RelativeLayout relative5 =  view.findViewById(R.id.button_with_image_above_text5);
        relative5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=5;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(5);

            }
        });
        RelativeLayout relative6 = view.findViewById(R.id.button_with_image_above_text6);
        relative6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=6;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(6);

            }
        });
        RelativeLayout relative7 =  view.findViewById(R.id.button_with_image_above_text7);
        relative7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=7;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(7);

            }
        });
        RelativeLayout relative8 =  view.findViewById(R.id.button_with_image_above_text8);
        relative8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=8;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(8);

            }
        });
        RelativeLayout relative9 =  view.findViewById(R.id.button_with_image_above_text9);
        relative9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                category=9;    primary.setVisibility(View.GONE);secondary.setVisibility(View.VISIBLE);fill_category(9);
            }
        });
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
    public  void fill_category(final int x) throws NullPointerException{
        list_of_projects.clear();
        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getProjects();
        call.enqueue(new Callback<List<ProjectObject>>() {
            @Override
            public void onResponse(Call<List<ProjectObject>> call, Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < editResponse.size(); i++) {
                        if (editResponse.get(0).getCategories() == x) {
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
                                    }
                                });

                            }
                            list_of_projects.add(temp);
                           recyclerAdapter.notifyItemInserted(recyclerAdapter.getItemCount() - 1);
                        }

                    }

                }
                addTimerActivity(list_of_projects , mRecylerView);
            }
            @Override
            public void onFailure(Call<List<ProjectObject>> call, Throwable t) {
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
