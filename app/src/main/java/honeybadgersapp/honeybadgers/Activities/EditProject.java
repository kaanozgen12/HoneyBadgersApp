package honeybadgersapp.honeybadgers.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import Adapters.ViewPagerAdapter;
import Fragments.Milestones_fragment;
import Fragments.Search_page_tab_fragment;
import honeybadgersapp.honeybadgers.R;

public class EditProject extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    private Milestones_fragment milestones_fragment =new Milestones_fragment();
    private  Search_page_tab_fragment trending_search= new Search_page_tab_fragment();
    private  Search_page_tab_fragment recommended_search= new Search_page_tab_fragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overridePendingTransition (0,0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_project);


        tabLayout = findViewById(R.id.edit_project_page_tablayout_id);
        viewPager = findViewById(R.id.edit_project_page_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(milestones_fragment, "Milestones");
        adapter.addFragment(trending_search, "Invoices");
        adapter.addFragment(recommended_search, "Files");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager,false);



        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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
