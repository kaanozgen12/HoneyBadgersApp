package honeybadgersapp.honeybadgers.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Dashboard_Notifications_adapter;
import Adapters.RecyclerItemTouchHelper;
import Models.Compact_Project_Object;
import RetrofitModels.Accepted_Project;
import RetrofitModels.Bid_Object;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProjectsActivity extends AppCompatActivity {
    public List<Compact_Project_Object> listOfProjects =new ArrayList<>();
    RecyclerView mRecylerView;
    Dashboard_Notifications_adapter recyclerAdapter;
    public Timer timer= new Timer();
     FloatingActionButton mButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_projects);
        mRecylerView= findViewById(R.id.my_projects_recyclerview);
        recyclerAdapter =new Dashboard_Notifications_adapter(this,listOfProjects);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setAdapter(recyclerAdapter);
        mButton = findViewById(R.id.my_projects_floatingActionButton);
        if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("freelancer")){
            mButton.hide();
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyProjectsActivity.this, CategoryActivity.class);
                //i.putExtra("project_id", "-1");
                startActivity(i);
                finish();
            }
        });


        Call<List<Accepted_Project>> call2 = RetrofitClient.getInstance().getApi().getMyAcceptedProjects(Integer.parseInt(LoginActivity.getCREDENTIALS()[4]));
        call2.enqueue(new Callback<List<Accepted_Project>>() {
            @Override
            public void onResponse(Call<List<Accepted_Project>> call2, Response<List<Accepted_Project>> response) {
                List<Accepted_Project> editResponse = response.body();
                Log.d("MyTag", "is response successful "+response.isSuccessful());
                if (response.isSuccessful()) {
                    Log.d("MyTag", "size response :"+response.body().size());
                    for (int i = 0; i < editResponse.size(); i++) {
                        Log.d("MyTag", "iiiiii "+editResponse.get(i).getId());
                        final Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).id,editResponse.get(i).getTitle(),""+editResponse.get(i).acceptedBid,"0",new ArrayList<Tag_Object>(),false,true);
                        listOfProjects.add(temp);
                        recyclerAdapter.notifyItemInserted(listOfProjects.size()-1);
                        Log.d("MyTag", "listofproject size  :"+listOfProjects.size());
                    }
                    Log.d("MyTag", "id :"+LoginActivity.getCREDENTIALS()[4]);
                    Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getMyProjects(Integer.parseInt(LoginActivity.getCREDENTIALS()[4]));
                    call.enqueue(new Callback<List<ProjectObject>>() {

                        @Override
                        public  void onResponse(@NonNull Call<List<ProjectObject>> call, @NonNull Response<List<ProjectObject>> response) {
                            List<ProjectObject> editResponse = response.body();
                            Log.d("MyTag", "is response successful "+response.isSuccessful());
                            if (response.isSuccessful()) {

                                Log.d("MyTag", "size response :"+response.body().size());
                                for (int i = 0; i < editResponse.size(); i++) {
                                    Log.d("MyTag", "iiiiii "+editResponse.get(i).getId());
                                    final ArrayList <Tag_Object> t = new ArrayList<>();
                                    final Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(),editResponse.get(i).getTitle(),editResponse.get(i).getBudgetMin()+"-"+editResponse.get(i).getBudgetMax(),"0",t,false,false);
                                    listOfProjects.add(temp);
                                    recyclerAdapter.notifyDataSetChanged();
                                    Log.d("MyTag", "listofproject size  :"+listOfProjects.size());
                                    for (int j =0 ; j<editResponse.get(i).getTags().length;j++){
                                        Call<Tag_Object> call2 = RetrofitClient.getInstance().getApi().getTag(editResponse.get(i).getTags()[j]);
                                        call2.enqueue(new Callback<Tag_Object>() {
                                            @Override
                                            public  void onResponse(@NonNull Call<Tag_Object> call2, @NonNull Response<Tag_Object> response2) {
                                                Tag_Object editResponse2 = response2.body();
                                                if (response2.isSuccessful()) {
                                                    t.add(editResponse2);
                                                    recyclerAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            @Override
                                            public  void onFailure(Call<Tag_Object> call2, Throwable t) {
                                            }
                                        });

                                    }

                                }
                            }
                        }
                        @Override
                        public  void onFailure(Call<List<ProjectObject>> call, Throwable t) {
                            Log.d("MyTag", "Search failed");
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Accepted_Project>> call2, Throwable t) {
            }
        });

       // recyclerAdapter.notifyDataSetChanged();
        SwipeControllerActions itemTouchHelperCallback =new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                new android.support.v7.app.AlertDialog.Builder(MyProjectsActivity.this)
                        .setTitle("DELETE?")
                        .setMessage("Are you sure you want to delete your project? Once you delete you will not be able to recover your project and all bidders will be notified" )
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Call<Void> call2 = RetrofitClient.getInstance().getApi().deleteProject("token "+LoginActivity.getCREDENTIALS()[0],listOfProjects.get(position).getId(),true);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public  void onResponse(@NonNull Call<Void> call2, @NonNull Response<Void> response2) {
                                        // remove the item from recycler view
                                        recyclerAdapter.removeItem(position);
                                    }
                                    @Override
                                    public  void onFailure(Call<Void> call2, Throwable t) {
                                        Log.d("MyTag","proje silme başarısız");
                                    }
                                });
                            }
                        }).create().show();
            }

            @Override
            public void onLeftClicked(int position) {
                Intent i = new Intent(MyProjectsActivity.this, EditProject.class);
                i.putExtra("project_id",""+listOfProjects.get(position).getId());
                i.putExtra("isVerified",(listOfProjects.get(position).getTags().size()==0?"verified":"notverified"));
                startActivity(i);
            }
        };
        final RecyclerItemTouchHelper swipeController = new RecyclerItemTouchHelper(itemTouchHelperCallback);
        mRecylerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        mRecylerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecylerView);
        addTimerActivity(listOfProjects,mRecylerView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.purge();
            timer.cancel();
            timer=null;
        }
    }
    //

    //
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("MyTag", "size listofprojects :"+listOfProjects.size());
    }

    public abstract static class SwipeControllerActions {

        public void onLeftClicked(int position) {}

        public void onRightClicked(int position) {}

    }
    public void addTimerActivity(final List<Compact_Project_Object> list_of_projects, final RecyclerView recycler) throws NullPointerException{

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
                                    runOnUiThread(new Runnable() {
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
