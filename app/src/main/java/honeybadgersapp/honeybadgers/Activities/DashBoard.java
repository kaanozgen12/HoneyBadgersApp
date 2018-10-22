package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import Adapters.RecyclerViewDashboard_Notifications;
import Models.Compact_Project_Object;
import honeybadgersapp.honeybadgers.R;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private NavigationView menu;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView DashBoardRecyclerView1;
    private RecyclerView DashBoardRecyclerView2;
    public List<Compact_Project_Object> list1 =new ArrayList<>();
    public List<Compact_Project_Object> list2 =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mDrawerlayout = findViewById(R.id.drawerLayout );
        final RelativeLayout board  = findViewById(R.id.dashboard_board);
        menu = findViewById(R.id.navigation_view);
        menu.inflateMenu(R.menu.navigation_drawer_items_freelancer);
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
                if (mDrawerlayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerlayout.closeDrawer(Gravity.RIGHT,false);
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mDrawerlayout.setElevation(-1);
                    Log.d("MyTag",""+mDrawerlayout.getElevation());
                    Log.d("MyTag",""+board.getElevation());
                }
                else {
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    mDrawerlayout.openDrawer(Gravity.RIGHT,false);
                    mDrawerlayout.setElevation(1);
                    Log.d("MyTag",""+mDrawerlayout.getElevation());
                    Log.d("MyTag",""+board.getElevation());
                }
            }
        });

        DashBoardRecyclerView1= (findViewById(R.id.dashboard_recyclerview_1));
        DashBoardRecyclerView2= (findViewById(R.id.dashboard_recyclerview_2));
        RecyclerViewDashboard_Notifications recyclerAdapter1 =new RecyclerViewDashboard_Notifications(this,list1);
        DashBoardRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView1.setAdapter(recyclerAdapter1);
        RecyclerViewDashboard_Notifications recyclerAdapter2 =new RecyclerViewDashboard_Notifications(this,list2);
        DashBoardRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        DashBoardRecyclerView2.setAdapter(recyclerAdapter2);


        ArrayList<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("C++");
        skills.add("Java");
        skills.add("C++");
        skills.add("Java");
        skills.add("C++");
        skills.add("Java");
        skills.add("C++");
        skills.add("Python");
        skills.add("C++");
        skills.add("Painting");
        skills.add("C++");
        list1.add(new Compact_Project_Object("Python project for skilled developers",100,"8 bids-30 minutes ago",skills));
        list1.add(new Compact_Project_Object("Python project for skilled developers",100,"8 bids-30 minutes ago",skills));
        list2.add(new Compact_Project_Object("Python project for skilled developers",100,"8 bids-30 minutes ago",skills));
        list2.add(new Compact_Project_Object("Python project for skilled developers",100,"8 bids-30 minutes ago",skills));
        list2.add(new Compact_Project_Object("Python project for skilled developers",100,"8 bids-30 minutes ago",skills));

    }

    private void setNavigationViewListener() {
        NavigationView navigationView =findViewById(R.id.navigation_view);
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
}