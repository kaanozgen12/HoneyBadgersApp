package honeybadgersapp.honeybadgers.Activities;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Milestones_adapter;
import RetrofitModels.Bid_Object;
import RetrofitModels.Milestone_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBidActivity extends AppCompatActivity {


    final List<Milestone_Object> milestones = new ArrayList<>();
     TextView total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_proposal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        total_amount= findViewById(R.id.bid_proposal_header_bottom_amount_text);
        RecyclerView milestones_recycler = findViewById(R.id.bid_proposal_recyclerview);

        LinearLayoutManager VerticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final Milestones_adapter recyclerAdapter = new Milestones_adapter(this,milestones);
        milestones_recycler.setLayoutManager(VerticalLayoutManager);
        milestones_recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        milestones_recycler.setItemAnimator(new DefaultItemAnimator());
        final EditText description=  findViewById(R.id.bid_proposal_edit_text);
        Button bidcompletebutton=findViewById(R.id.bid_proposal_bid_button);
        bidcompletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MyTag","!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                int temp=0;
                for (int x= 0 ; x<milestones.size(); x++){
                    temp+= milestones.get(x).getAmount();
                }
                Call<Bid_Object> call6 = RetrofitClient.getInstance().getApi().bidOnProject("token "+LoginActivity.getCREDENTIALS()[0],getIntent().getIntExtra("project_id",-1),description.getText().toString(),temp);
                call6.enqueue(new Callback<Bid_Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Bid_Object> call6, @NonNull Response<Bid_Object> response2) {
                        Bid_Object editResponse2 = response2.body();
                        if (response2.isSuccessful()) {
                            Log.d("MyTag","SUCCESSFUL BID CREATION");

                            for (int k= 0; k< milestones.size(); k++){
                                Call<Milestone_Object> call0 = RetrofitClient.getInstance().getApi().milestoneOnBid("token "+LoginActivity.getCREDENTIALS()[0],editResponse2.getId(),milestones.get(k).getDescription(),milestones.get(k).getAmount(),milestones.get(k).getDeadline());
                                call0.enqueue(new Callback<Milestone_Object>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Milestone_Object> call0, @NonNull Response<Milestone_Object> response3) {
                                        Log.d("MyTag","SUCCESSFUL MILESTONE CREATION");
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<Milestone_Object> call0, @NonNull Throwable t) {
                                        Log.d("MyTag","UNSUCCESSFUL MILESTONE CREATION");
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Bid_Object> call6, Throwable t) {
                        Log.d("MyTag","UNSUCCESSFUL BID CREATION");
                    }
                });
                //Toast.makeText( "You have successfully bid on the project", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton add_button = findViewById(R.id.floatingActionButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog myDialog2 = new Dialog(AddBidActivity.this);
                myDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog2.setContentView(R.layout.milestone_adder);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(myDialog2.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                myDialog2.getWindow().setAttributes(lp);
                Button save_button = myDialog2.findViewById(R.id.milestone_adder_save_button);
                final EditText description = myDialog2.findViewById(R.id.milestone_adder_description);
                final EditText amount  = myDialog2.findViewById(R.id.milestone_adder_amount);
                final DatePicker deadlineDay = myDialog2.findViewById(R.id.milestone_adder_date_picker);
                final TimePicker deadlineTime = myDialog2.findViewById(R.id.milestone_adder_time_picker);

                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View focusView = null;
                        boolean cancel = false;

                        if (TextUtils.isEmpty(description.getText().toString())) {
                            description.setError(getApplicationContext().getString(R.string.error_field_required));
                            focusView = description;
                            cancel = true;
                        }else if (TextUtils.isEmpty(amount.getText().toString())) {
                            amount.setError(getApplicationContext().getString(R.string.error_field_required));
                            focusView = amount;
                            cancel = true;
                        }if (cancel) {
                            // There was an error; don't attempt save and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                        } else {
                            int year = deadlineDay.getYear();
                            int month = deadlineDay.getMonth();
                            int day = deadlineDay.getDayOfMonth();
                            int hour = deadlineTime.getCurrentHour();
                            int minute = deadlineTime.getCurrentMinute();
                            String deadline= ""+year+"-"+month+"-"+day+"T"+hour+":"+minute+":00"+"Z";

                            milestones.add(new Milestone_Object(Integer.parseInt(amount.getText().toString()),description.getText().toString(),deadline));
                            recyclerAdapter.notifyDataSetChanged();
                            myDialog2.dismiss();
                        }
                    }
                });
                myDialog2.show();
            }
        });


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int temp = 0;
                for (int x = 0; x < milestones.size(); x++) {
                    temp += milestones.get(x).getAmount();
                }
                final int finalTemp = temp;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        total_amount.setText(finalTemp +" ");
                    }
                });
            }
        }, 0, 1000);

       /* private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

            }
        },1, TimeUnit.SECONDS);*/


    }
    private class AsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void params) {

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            while (true) {

               /* final int finalTemp = temp;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        total_amount.setText(finalTemp+" ");
                    }
                },1000);*/
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


}
