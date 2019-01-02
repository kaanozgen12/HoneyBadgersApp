package honeybadgersapp.honeybadgers.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Dashboard_Notifications_adapter;
import FirebaseClasses.OnlineUsers;
import Models.Compact_Project_Object;
import RetrofitModels.Bid_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.ProjectObject;
import RetrofitModels.Recommended_Project_Object;
import RetrofitModels.Tag_Object;
import RetrofitModels.Wallet;
import api.RetrofitClient;
import api.SimpleCredentialCrypting;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private ImageButton mButton;
    private RecyclerView DashBoardRecyclerView1;
    private RecyclerView DashBoardRecyclerView2;
    private Dashboard_Notifications_adapter recyclerAdapter1;
    private Dashboard_Notifications_adapter recyclerAdapter2;
    public  ImageView logo;
    public  TextView user_email;
    public  TextView user_name;
    public  Switch user_role;
    public TextView money;
    public   NavigationView navigationView;
    public View headerView ;
    public List<Compact_Project_Object> list1 =new ArrayList<>();
    public List<Compact_Project_Object> list2 =new ArrayList<>();
    private  SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SimpleCredentialCrypting(this, this.getSharedPreferences("HONEY_BADGERS_PREFS_FILE", Context.MODE_PRIVATE) );
        Firebase.setAndroidContext(this);
        overridePendingTransition (0,0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);
        Firebase.setAndroidContext(this);
        mDrawerlayout = findViewById(R.id.drawerLayout );
        final RelativeLayout board  = findViewById(R.id.dashboard_board);
        setNavigationViewListener();
        headerView =navigationView.getHeaderView(0);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.Open,R.string.Close);
        mDrawerlayout.addDrawerListener(mToggle);
//        mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggle.syncState();
        mToolbar = findViewById(R.id.action_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide built-in Title
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_person_orange_24dp);
        ImageButton imageViewMenu =  findViewById(R.id.toggle);


        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerlayout.isDrawerOpen(Gravity.END)) {
                    mDrawerlayout.closeDrawer(Gravity.END,true);
                    /*mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mDrawerlayout.setElevation(-1);*/
                }
                else {
                    mDrawerlayout.openDrawer(Gravity.END,true);
//                    mDrawerlayout.setElevation(1);

                }
            }
        });
        ImageButton mButton = findViewById(R.id.filter_button) ;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoard.this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = DashBoard.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.filter_dialog, null);
                dialogBuilder.setView(dialogView);

                EditText editText = (EditText) dialogView.findViewById(R.id.min_budget_edit);
                editText.setText("0");
                EditText editText2 = (EditText) dialogView.findViewById(R.id.max_budget_edit);
                editText2.setText("0");
                Spinner mSpinner = dialogView.findViewById(R.id.ordering_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialogView.getContext(), R.array.Ordering, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(adapter);
                Button filterButton = (Button) dialogView.findViewById(R.id.filter_button2);

                Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                cancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }

                });

            }
        });

        DashBoardRecyclerView1= (findViewById(R.id.dashboard_recyclerview_1));
        DashBoardRecyclerView2= (findViewById(R.id.dashboard_recyclerview_2));
        recyclerAdapter1 =new Dashboard_Notifications_adapter(this,list1);
        DashBoardRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView1.setAdapter(recyclerAdapter1);

        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getProjectsRecent();
        call.enqueue(new Callback<List<ProjectObject>>() {
            @Override
            public void onResponse(Call<List<ProjectObject>> call, Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
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
                                        recyclerAdapter1.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Tag_Object> call2, Throwable t) {
                                }
                            });

                        }
                        list1.add(temp);
                        recyclerAdapter1.notifyItemInserted(recyclerAdapter1.getItemCount() - 1);
                        if(i==4){
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ProjectObject>> call, Throwable t) {
            }
        });

       recyclerAdapter2 =new Dashboard_Notifications_adapter(this,list2);
        DashBoardRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView2.setAdapter(recyclerAdapter2);

        logo = headerView.findViewById(R.id.person_logo);
        user_email= headerView.findViewById(R.id.useremail_text);
        user_name= headerView.findViewById(R.id.username_text);
        user_role = headerView.findViewById(R.id.switch_profile);
        money = headerView.findViewById(R.id.money_text);
        if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("Client")){
            user_role.setChecked(true);
        }
        user_role.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                user_role.setText(((LoginActivity.getCREDENTIALS()[3].equals("Freelancer"))?"Client":"Freelancer"));
                LoginActivity.getCREDENTIALS()[3]=((LoginActivity.getCREDENTIALS()[3].equals("Freelancer"))?"Client":"Freelancer");

                Call<Void> call2 = RetrofitClient.getInstance().getApi().userRegisterRoleChange("token "+LoginActivity.getCREDENTIALS()[0],Integer.parseInt(LoginActivity.getCREDENTIALS()[4]),(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("Freelancer")?0:1));
                call2.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call2, Response<Void> response2) {

                    }
                    @Override
                    public void onFailure(Call<Void> call2, Throwable t) {

                    }
                });

                mDrawerlayout.closeDrawer(Gravity.END,true);
                setNavigationViewListener();
                onResume();

            }
        });
        user_name.setText(LoginActivity.getCREDENTIALS()[2]);
        addTimerActivity(new Timer(),list1,DashBoardRecyclerView1);
        addTimerActivity(new Timer(),list2,DashBoardRecyclerView2);


    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(Gravity.END)) {
            mDrawerlayout.closeDrawer(Gravity.END,false);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            DashBoard.super.onBackPressed();
                        }
                    }).create().show();
        }

    }

    private void setNavigationViewListener() {
        navigationView =findViewById(R.id.navigation_view);
        navigationView.getMenu().clear();
        navigationView.setNavigationItemSelectedListener(this);
        if(LoginActivity.getCREDENTIALS()[3].equals("Freelancer")){
            navigationView.inflateMenu(R.menu.navigation_drawer_items_freelancer);
        }else if(LoginActivity.getCREDENTIALS()[3].equals("Client")){
            navigationView.inflateMenu(R.menu.navigation_drawer_items_client);
        }
    }

    public boolean onNavigationItemSelected( MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.item_navigation_drawer_edit_profile: {
                Intent i = new Intent(DashBoard.this, EditProfile.class);
                startActivity(i);
                break;
            }
            case R.id.item_navigation_drawer_create_project: {
                Intent i = new Intent(DashBoard.this, CategoryActivity.class);
                startActivity(i);
                break;
            }
            case R.id.item_navigation_drawer_log_out: {
                Intent i = new Intent(DashBoard.this, LoginActivity.class);
                SharedPreferences prefs = new SimpleCredentialCrypting(this, this.getSharedPreferences("HONEY_BADGERS_PREFS_FILE", Context.MODE_PRIVATE) );
                prefs.edit().clear().commit();
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DashBoard.this,
                                        "You have been signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                startActivity(i);
                finish();
                break;
            }case R.id.item_navigation_drawer_search: {
                Intent i = new Intent(DashBoard.this, SearchActivity.class);
                startActivity(i);
                break;
            }case R.id.item_navigation_drawer_my_projects: {
                Intent i = new Intent(DashBoard.this, MyProjectsActivity.class);
                startActivity(i);
                break;
            }case R.id.messaging_inbox: {
                Intent i = new Intent(DashBoard.this, OnlineUsers.class);
                startActivity(i);
                break;
            }
            default:
                break;
        }
        //close navigation drawer
        mDrawerlayout.closeDrawer(Gravity.RIGHT,false);
        mDrawerlayout.setElevation(-1);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<List<ProfileObject>> call;
        if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("freelancer")){
            call = RetrofitClient.getInstance().getApi().freelancerProfileGet("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[4]);

        }else{
            call= RetrofitClient.getInstance().getApi().clientProfileGet("token "+LoginActivity.getCREDENTIALS()[0],LoginActivity.getCREDENTIALS()[4]);
        }
        call.enqueue(new Callback<List<ProfileObject>>() {
            @Override
            public void onResponse(Call<List<ProfileObject>> call, Response<List<ProfileObject>> response) {
                List<ProfileObject> editResponse = response.body();
                if (response.isSuccessful()) {
                    if (editResponse != null && editResponse.size() >0 && editResponse.get(0).getAvatar() != null) {
                        new SendHttpRequestTask().execute(editResponse.get(0).getAvatar());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProfileObject>> call, Throwable t) {
                Log.d("MyTag","Failed");
            }
        });
        if(LoginActivity.getCREDENTIALS()[1]!=null){
            user_email.setText(LoginActivity.getCREDENTIALS()[1]);
        }if(LoginActivity.getCREDENTIALS()[3]!=null){
            user_role.setText(LoginActivity.getCREDENTIALS()[3]);
        }if(LoginActivity.getCREDENTIALS()[2]!=null){
            user_name.setText(LoginActivity.getCREDENTIALS()[2]);
        }

        Call<Wallet> call2 = RetrofitClient.getInstance().getApi().getUserWallet(Integer.parseInt(LoginActivity.getCREDENTIALS()[4]));
        call2.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(@NonNull Call<Wallet> call2, @NonNull Response<Wallet> response2) {
                Wallet editResponse2 = response2.body();
                if (response2.isSuccessful()&&editResponse2!=null) {
                    Log.d("MyTag","id for wallet: "+LoginActivity.getCREDENTIALS()[4]);
                    money.setText(""+editResponse2.getBudget());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Wallet> call2, Throwable t) {
            }
        });

        if(LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("Freelancer")) {
            list2.clear();
            recyclerAdapter2.notifyDataSetChanged();
            Call<List<Recommended_Project_Object>> call3 = RetrofitClient.getInstance().getApi().getRecommendedProjects("token " + LoginActivity.getCREDENTIALS()[0]);
            call3.enqueue(new Callback<List<Recommended_Project_Object>>() {
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
                                            recyclerAdapter2.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Tag_Object> call2, Throwable t) {
                                    }
                                });

                            }
                            list2.add(temp);
                            recyclerAdapter2.notifyItemInserted(recyclerAdapter2.getItemCount() - 1);
                            if (i == 4) {
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Recommended_Project_Object>> call, Throwable t) {
                }
            });
        }else{
            list2.clear();
            recyclerAdapter2.notifyDataSetChanged();

        }

    }

    public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                //
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null)
                logo.setImageBitmap(result);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void addTimerActivity(Timer timer,final List<Compact_Project_Object> list_of_projects, final RecyclerView recycler) throws NullPointerException{
        timer =new Timer();
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
                Log.d("MyTag","::::::firstvisible::::: "+firstVisiblePosition+"last::::: "+list_of_projects.size());

                if(list_of_projects.size() > 0) {
                    for (int i = firstVisiblePosition; i < lastVisiblePosition; i++) {
                        final Calendar cal = Calendar.getInstance();
                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                        final long[] difference = new long[1];
                        Log.d("MyTag",":::1"+list_of_projects.size() );
                        Log.d("MyTag",":::i "+i );
                        Call<List<Bid_Object>> call0 = RetrofitClient.getInstance().getApi().getAllBidsOfProject("token " + LoginActivity.getCREDENTIALS()[0], list_of_projects.get(i).getId());
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

                                    if(list_of_projects.size() > 0) {
                                        break;
                                    }

                                }
                                Log.d("MyTag",":::2"+list_of_projects.size());

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
                                Log.d("MyTag",":::3"+list_of_projects.size());
                                if (list_of_projects.size() >0 && list_of_projects.get(finalI).isMoneySignFirst() && ((recycler.findViewHolderForAdapterPosition(finalI)) != null)&& editresponse.size() > Integer.parseInt(list_of_projects.get(finalI).getNumberofbidsandlastupdate().substring(0, list_of_projects.get(finalI).getNumberofbidsandlastupdate().indexOf(" ")))) {
                                    Log.d("MyTag", "----------------------------------------");
                                    final String finalTime = time;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setAlpha(0f);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setVisibility(View.VISIBLE);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().animate()
                                                        .alpha(1f)
                                                        .setDuration(1200)
                                                        .setListener(null);
                                                recycler.getAdapter().notifyItemChanged(finalI);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setAlpha(1f);
                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().animate()
                                                        .alpha(0f)
                                                        .setDuration(1200)
                                                        .setListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getMoney_sign().setVisibility(View.GONE);
                                                                ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().setText(String.format("%d bids - %s", editresponse.size(), finalTime));
                                                            }
                                                        });
                                                recycler.getAdapter().notifyItemChanged(finalI);
                                            }catch (NullPointerException e){
                                                Log.d("MyTag","null pointer dereferenced");
                                            }
                                        }

                                    });
                                }
                               /* if ((recycler.findViewHolderForAdapterPosition(finalI)) != null && editresponse.size() == Integer.parseInt(list_of_projects.get(finalI).getNumberofbidsandlastupdate().substring(0, list_of_projects.get(finalI).getNumberofbidsandlastupdate().indexOf(" ")))) {
                                    Log.d("MyTag", "----------------------------------------1");
                                    stable[0] = true;
                                    ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().setVisibility(View.VISIBLE);

                                } else {
                                    Log.d("MyTag", "----------------------------------------2");
                                    stable[0] = false;
                                }*/
                                Log.d("MyTag",":::4"+list_of_projects.size());
                                if(list_of_projects.size() > 0) {


                                    list_of_projects.get(finalI).setMoneySignFirst(true);

                                    try {
                                        if ((recycler.findViewHolderForAdapterPosition(finalI)) != null && !((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().getText().toString().equals(String.format("%d bids - %s", editresponse.size(), time))) {
                                            ((Dashboard_Notifications_adapter.MyViewHolder) recycler.findViewHolderForAdapterPosition(finalI)).getUpdateinfo().setText(String.format("%d bids - %s", editresponse.size(), time));
                                            list_of_projects.get(finalI).setNumberofbidsandlastupdate(String.format("%d bids - %s", editresponse.size(), time));
                                        }
                                    }catch (NullPointerException e){
                                        Log.d("MyTag","null pointer dereferencing caught");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<Bid_Object>> call0, @NonNull Throwable t) {
                            }
                        });
                    }

                }
            }

        }, 0, 1000);
    }
}