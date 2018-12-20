package honeybadgersapp.honeybadgers.Activities;

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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.List;

import Adapters.Dashboard_Notifications_adapter;
import FirebaseClasses.OnlineUsers;
import Models.Compact_Project_Object;
import RetrofitModels.ProfileObject;
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
    private RecyclerView DashBoardRecyclerView1;
    private RecyclerView DashBoardRecyclerView2;
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

        DashBoardRecyclerView1= (findViewById(R.id.dashboard_recyclerview_1));
        DashBoardRecyclerView2= (findViewById(R.id.dashboard_recyclerview_2));
        Dashboard_Notifications_adapter recyclerAdapter1 =new Dashboard_Notifications_adapter(this,list1);
        DashBoardRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView1.setAdapter(recyclerAdapter1);
        Dashboard_Notifications_adapter recyclerAdapter2 =new Dashboard_Notifications_adapter(this,list2);
        DashBoardRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView2.setAdapter(recyclerAdapter2);

        ArrayList<Tag_Object> temp1= new ArrayList<Tag_Object>();
        ArrayList<Tag_Object> temp2= new ArrayList<Tag_Object>();
        ArrayList<Tag_Object> temp3= new ArrayList<Tag_Object>();
        temp1.add(new Tag_Object(1,"HTML"));
        temp1.add(new Tag_Object(2,"PHP"));
        temp1.add(new Tag_Object(3,"CSS"));
        temp2.add(new Tag_Object(4,"3D Design"));
        temp2.add(new Tag_Object(5,"Photoshop"));
        temp3.add(new Tag_Object(6,"Article Writing"));

        list2.add(new Compact_Project_Object(999,"We need help in HTML design","50-100","2 bids-3 hours ago",temp1,false,false));
        list2.add(new Compact_Project_Object(999,"We are looking for a 3D designer","500-600","3 bids-3 hours ago",temp2,false,false));
        list1.add(new Compact_Project_Object(999,"We have 10 excel files","20-40","4 bids-3 hours ago",temp3,false,false));

        logo = headerView.findViewById(R.id.person_logo);
        user_email= headerView.findViewById(R.id.useremail_text);
        user_name= headerView.findViewById(R.id.username_text);
        user_role = headerView.findViewById(R.id.switch_profile);
        money = headerView.findViewById(R.id.money_text);
        if(prefs.getString("userrole",null)!=null && prefs.getString("userrole",null).equalsIgnoreCase("Client")){
            LoginActivity.getCREDENTIALS()[3]="Client";
            user_role.setChecked(true);
        }else {
            LoginActivity.getCREDENTIALS()[3]="Freelancer";
        }
        user_role.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                user_role.setText(((LoginActivity.getCREDENTIALS()[3].equals("Freelancer"))?"Client":"Freelancer"));
                prefs.edit().remove("userrole").commit();
                prefs.edit().putString("userrole",((LoginActivity.getCREDENTIALS()[3].equalsIgnoreCase("Freelancer"))?"Client":"Freelancer")).commit();

                LoginActivity.getCREDENTIALS()[3]=((LoginActivity.getCREDENTIALS()[3].equals("Freelancer"))?"Client":"Freelancer");
                mDrawerlayout.closeDrawer(Gravity.END,true);
                setNavigationViewListener();
                Log.d("MyTag",prefs.getString("userrole",null));
            }
        });
        user_name.setText(LoginActivity.getCREDENTIALS()[2]);


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
                Intent i = new Intent(DashBoard.this, CreateProject.class);
                i.putExtra("project_id","-1");
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
}