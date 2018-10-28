package honeybadgersapp.honeybadgers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.RecyclerViewDashboard_Notifications;
import Models.Compact_Project_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import api.SimpleCredentialCrypting;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private NavigationView menu;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView DashBoardRecyclerView1;
    private RecyclerView DashBoardRecyclerView2;
    public static ImageView logo;
    public static TextView user_email;
    public static TextView user_name;
    public static Switch user_role;
    public static NavigationView navigationView;
    public List<Compact_Project_Object> list1 =new ArrayList<>();
    public List<Compact_Project_Object> list2 =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);
        mDrawerlayout = findViewById(R.id.drawerLayout );
        LoginActivity.setCREDENTIALSROLE("Client");
        final RelativeLayout board  = findViewById(R.id.dashboard_board);
        menu = findViewById(R.id.navigation_view);
        if(LoginActivity.getCREDENTIALS()[3].equals("Freelancer")){
            menu.inflateMenu(R.menu.navigation_drawer_items_freelancer);
        }else if(LoginActivity.getCREDENTIALS()[3].equals("Client")){
            menu.inflateMenu(R.menu.navigation_drawer_items_client);
        }

        setNavigationViewListener();
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.Open,R.string.Close);
        mDrawerlayout.addDrawerListener(mToggle);
        mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
                    mDrawerlayout.closeDrawer(Gravity.END,false);
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mDrawerlayout.setElevation(-1);
                }
                else {
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    mDrawerlayout.openDrawer(Gravity.END,false);
                    mDrawerlayout.setElevation(1);

                }
            }
        });

        synchronized (this) {
           /* Call<List<ProjectObject>> call2 = RetrofitClient.getInstance().getApi().getProjects();
            call2.enqueue(new Callback<List<ProjectObject>>() {
                @Override
                public void onResponse(Call<List<ProjectObject>> call2, Response<List<ProjectObject>> response) {
                    List<ProjectObject> editResponse = response.body();
                    ArrayList<Compact_Project_Object> temp = new ArrayList<>();
                    for (int i = 0; i < editResponse.size(); i++) {
                        list2.add(editResponse.get(i).compress());
                    }
                }

                @Override
                public void onFailure(Call<List<ProjectObject>> call, Throwable t) {
                }
            });*/
        }
        DashBoardRecyclerView1= (findViewById(R.id.dashboard_recyclerview_1));
        DashBoardRecyclerView2= (findViewById(R.id.dashboard_recyclerview_2));
        RecyclerViewDashboard_Notifications recyclerAdapter1 =new RecyclerViewDashboard_Notifications(this,list1);
        DashBoardRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView1.setAdapter(recyclerAdapter1);
        RecyclerViewDashboard_Notifications recyclerAdapter2 =new RecyclerViewDashboard_Notifications(this,list2);
        DashBoardRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView2.setAdapter(recyclerAdapter2);
        View headerView =navigationView.getHeaderView(0);

        DashBoardRecyclerView1.getAdapter().notifyDataSetChanged();
        DashBoardRecyclerView2.getAdapter().notifyDataSetChanged();

        logo = headerView.findViewById(R.id.person_logo);
        user_email= headerView.findViewById(R.id.useremail_text);
        user_name = headerView.findViewById(R.id.username_text);
        user_role = headerView.findViewById(R.id.switch_profile);

        ArrayList<Tag_Object> skills = new ArrayList<>();
        skills.add(new Tag_Object(0,"Java"));
        skills.add(new Tag_Object(1,"C++"));

        list1.add(new Compact_Project_Object(1,"Python project for skilled developers",0,"100",skills));
        list1.add(new Compact_Project_Object(2,"Python project for skilled developers",0,"100",skills));
        list1.add(new Compact_Project_Object(3,"Python project for skilled developers",0,"100",skills));
        list2.add(new Compact_Project_Object(4,"Python project for skilled developers",0,"100",skills));
        list2.add(new Compact_Project_Object(5,"Python project for skilled developers",0,"100",skills));


    }

    private void setNavigationViewListener() {
        navigationView =findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                startActivity(i);
                break;
            }
            case R.id.item_navigation_drawer_log_out: {
                Intent i = new Intent(DashBoard.this, LoginActivity.class);
                SharedPreferences prefs = new SimpleCredentialCrypting(this, this.getSharedPreferences("HONEY_BADGERS_PREFS_FILE", Context.MODE_PRIVATE) );
                prefs.edit().clear().commit();
                startActivity(i);
                finish();
                break;
            }case R.id.item_navigation_drawer_search: {
                Intent i = new Intent(DashBoard.this, SearchActivity.class);
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
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerlayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerlayout.closeDrawer(Gravity.RIGHT);
            }
            else {
                mDrawerlayout.openDrawer(Gravity.RIGHT);
            }
        }
        return false;
    }
    // Menu icons are inflated just as they were with actionbar
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_items, menu);
        return true;
    }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (this) {
            Call<ProfileObject> call = RetrofitClient.getInstance().getApi().userProfileGet("token " + LoginActivity.getCREDENTIALS()[0], LoginActivity.getCREDENTIALS()[4]);
            call.enqueue(new Callback<ProfileObject>() {
                @Override
                public void onResponse(Call<ProfileObject> call, Response<ProfileObject> response) {
                    ProfileObject editResponse = response.body();
                    if (response.isSuccessful()) {
                        if (editResponse.getAvatar() != null) {
                            DashBoard.logo.setBackground((Drawable) editResponse.getAvatar());
                        }
                        DashBoard.user_name.setText(editResponse.getName());
                    }
                }

                @Override
                public void onFailure(Call<ProfileObject> call, Throwable t) {

                }
            });
        }
        if(LoginActivity.getCREDENTIALS()[1]!=null){
            user_email.setText(LoginActivity.getCREDENTIALS()[1]);
        }if(LoginActivity.getCREDENTIALS()[3]!=null){
            user_role.setText(LoginActivity.getCREDENTIALS()[3]);
        }




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}