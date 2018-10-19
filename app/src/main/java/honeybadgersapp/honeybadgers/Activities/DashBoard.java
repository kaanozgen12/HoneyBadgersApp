package honeybadgersapp.honeybadgers.Activities;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import honeybadgersapp.honeybadgers.R;

public class DashBoard extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView DashBoardRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mDrawerlayout = findViewById(R.id.drawerLayout );
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.Open,R.string.Close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide built-in Title
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_person_orange_24dp);
        ImageButton imageViewMenu = (ImageButton)  findViewById(R.id.toggle);

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerlayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerlayout.closeDrawer(Gravity.RIGHT);
                }
                else {
                    mDrawerlayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        DashBoardRecyclerView= (findViewById(R.id.dashboard_recyclerview));
    }

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
    }*/
    // Menu icons are inflated just as they were with actionbar
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_items, menu);
        return true;
    }*/
}