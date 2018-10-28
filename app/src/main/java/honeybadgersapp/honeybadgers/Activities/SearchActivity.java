package honeybadgersapp.honeybadgers.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import Adapters.ViewPagerAdapter;
import Models.Search_page_tab_fragment;
import RetrofitModels.ProjectObject;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private DrawerLayout mDrawerlayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggleSettings;
    private ImageView mToggleBack;
    private Search_page_tab_fragment category_search =new Search_page_tab_fragment();
    private  Search_page_tab_fragment trending_search= new Search_page_tab_fragment();
    private  Search_page_tab_fragment recommended_search= new Search_page_tab_fragment();
    boolean first;
    boolean second;
    boolean third;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        first= second= third = true;
        mDrawerlayout = findViewById(R.id.search_page_drawerLayout );
        tabLayout = findViewById(R.id.search_page_tablayout_id);
        viewPager = findViewById(R.id.search_page_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment( category_search, "Categories");
        adapter.addFragment(trending_search, "Trending");
        adapter.addFragment(recommended_search, "Recommended");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        mToggleSettings = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.Open,R.string.Close);
        mDrawerlayout.addDrawerListener(mToggleSettings);
        mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggleSettings.syncState();
        mToggleBack = findViewById(R.id.search_page_back_toggle);
        mToolbar = findViewById(R.id.search_page_action_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hide built-in Title
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        ImageButton imageViewMenu =  findViewById(R.id.search_page_advanced_search_toggle);

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
        mToggleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
                if(second&&i==1){
                    fill_trending();
                    second=false;
                }

                Log.d("MyTag","PAge şuan kaçta:"+i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.setupWithViewPager(viewPager,true);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_content_copy_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_inbox_black_24dp);


        //getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();




    }
    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public synchronized void fill_trending(){
        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.clear();
        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getProjects();
        call.enqueue(new Callback<List<ProjectObject>>() {
            @Override
            public void onResponse(Call<List<ProjectObject>> call, Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < editResponse.size(); i++) {

                        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.add(editResponse.get(i).compress());
                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemInserted(i);
                        //((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemChanged(i);
                       // getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();

                    }
                }

                //getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();
                Log.d("MyTag", "Search success: " + editResponse.size());
            }

            @Override
            public void onFailure(Call<List<ProjectObject>> call, Throwable t) {

                Log.d("MyTag", "Search failed");
            }
        });
    }

}
