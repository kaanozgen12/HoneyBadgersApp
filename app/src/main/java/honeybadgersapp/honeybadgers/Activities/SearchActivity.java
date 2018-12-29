package honeybadgersapp.honeybadgers.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.Dashboard_Notifications_adapter;
import Adapters.ViewPagerAdapter;
import Fragments.Categories_Fragment;
import Fragments.Search_page_tab_fragment;
import Models.Compact_Project_Object;
import RetrofitModels.Bid_Object;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private TabLayout tabLayout;
    private DrawerLayout mDrawerlayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    private ActionBarDrawerToggle mToggleSettings;
    private ImageView mToggleBack;
    private Spinner mSpinner;
    private Categories_Fragment category_search =new Categories_Fragment();
    private  Search_page_tab_fragment trending_search= new Search_page_tab_fragment();
    private  Search_page_tab_fragment recommended_search= new Search_page_tab_fragment();
    private SearchView editsearch;
    boolean first;
    boolean second;
    boolean third;
    Timer timer_treanding = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        overridePendingTransition (0,0);
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
        tabLayout.setupWithViewPager(viewPager,false);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_content_copy_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_inbox_black_24dp);
        fill_trending();
        mToggleSettings = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.Open,R.string.Close);
        mDrawerlayout.addDrawerListener(mToggleSettings);
        mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggleSettings.syncState();
        mToggleBack = findViewById(R.id.search_page_back_toggle);
        Toolbar mToolbar = findViewById(R.id.search_page_action_toolbar);
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
        editsearch = (SearchView) findViewById(R.id.search_page_search_bar);
        editsearch.setOnQueryTextListener(this);


        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
                if(second&&i==1){
                    second=false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mSpinner = findViewById(R.id.ordering_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Ordering, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        /*mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = ""+(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "1";
            }
        });*/


        //getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.setAdapter(null);
        if(timer_treanding!=null){
            timer_treanding.purge();
            timer_treanding.cancel();
            timer_treanding=null;
        }
        try{
            category_search.onDestroy();
            recommended_search.onDestroy();
            trending_search.onDestroy();
        }catch (NullPointerException e){}

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public  void fill_trending() throws NullPointerException{
        //((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.clear();
        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getProjects();
        call.enqueue(new Callback<List<ProjectObject>>() {
            @Override
            public void onResponse(Call<List<ProjectObject>> call, Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < editResponse.size(); i++) {
                        final ArrayList <Tag_Object> t = new ArrayList<>();

                        Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(),editResponse.get(i).getTitle(),editResponse.get(i).getBudgetMin()+"-"+editResponse.get(i).getBudgetMax(),"0",t,false,false);

                        for (int j =0 ; j<editResponse.get(i).getTags().length;j++){
                            Call<Tag_Object> call2 = RetrofitClient.getInstance().getApi().getTag(editResponse.get(i).getTags()[j]);
                            call2.enqueue(new Callback<Tag_Object>() {
                                @Override
                                public void onResponse(Call<Tag_Object> call2, Response<Tag_Object> response) {
                                    Tag_Object editResponse = response.body();
                                    if (response.isSuccessful()) {
                                        t.add(editResponse);
                                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Tag_Object> call2, Throwable t) {
                                }
                            });

                        }
                        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.add(temp);
                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemInserted(((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.getItemCount()-1);
                    }
                    addTimerActivity(((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects,((Search_page_tab_fragment)adapter.getItem(1)).myrecyclerview);
      /*              for (int i = 0; i < editResponse.size(); i++) {
                        Compact_Project_Object temp;
                            temp= editResponse.get(i).compress();
                            ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.add(temp);

                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemInserted(i);
                        //((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemChanged(i);
                       // getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();
                   */
                }

                //getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();
                // Log.d("MyTag", "Search success: " + editResponse.size());
            }
            @Override
            public void onFailure(Call<List<ProjectObject>> call, Throwable t) {
                Log.d("MyTag", "Search failed "+ t.getMessage());
            }
        });

    }
    public  void filter_trending(String search_name) throws NullPointerException{
        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.clear();
        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().search(search_name);
        call.enqueue(new Callback<List<ProjectObject>>() {
            @Override
            public void onResponse(Call<List<ProjectObject>> call, Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
                if (response.isSuccessful()) {
                    for (int i = 0; i < editResponse.size(); i++) {
                        final ArrayList <Tag_Object> t = new ArrayList<>();

                        Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(),editResponse.get(i).getTitle(),editResponse.get(i).getBudgetMin()+"-"+editResponse.get(i).getBudgetMax(),"0",t,false,false);
                        Log.d("MyTag", "onResponse: " + editResponse.get(i).getTitle());
                        for (int j =0 ; j<editResponse.get(i).getTags().length;j++){
                            Call<Tag_Object> call2 = RetrofitClient.getInstance().getApi().getTag(editResponse.get(i).getTags()[j]);
                            call2.enqueue(new Callback<Tag_Object>() {
                                @Override
                                public void onResponse(Call<Tag_Object> call2, Response<Tag_Object> response) {
                                    Tag_Object editResponse = response.body();
                                    if (response.isSuccessful()) {
                                        t.add(editResponse);
                                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Tag_Object> call2, Throwable t) {
                                }
                            });

                        }
                        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.add(temp);
                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemInserted(((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.getItemCount()-1);
                    }
                    addTimerActivity(((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects,((Search_page_tab_fragment)adapter.getItem(1)).myrecyclerview);
                    for (int i = 0; i < editResponse.size(); i++) {
                        Log.d("MyTag", "onResponse: " + editResponse.size());
                        final ArrayList <Tag_Object> t = new ArrayList<>();
                        Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(),editResponse.get(i).getTitle(),editResponse.get(i).getBudgetMin()+"-"+editResponse.get(i).getBudgetMax(),"0",t,false,false);

                        ((Search_page_tab_fragment)adapter.getItem(1)).list_of_projects.add(temp);

                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemInserted(i);
                        ((Search_page_tab_fragment)adapter.getItem(1)).recyclerAdapter.notifyItemChanged(i);
                       // getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();
                       }

                }

                getSupportFragmentManager().beginTransaction().detach(adapter.getItem(1)).attach(adapter.getItem(1)).commit();
                 Log.d("MyTag", "Search success: " + editResponse.size());
            }
            @Override
            public void onFailure(Call<List<ProjectObject>> call, Throwable t) {
                Log.d("MyTag", "Search failed "+ t.getMessage());
            }
        });

    }

    public void addTimerActivity(final List<Compact_Project_Object> list_of_projects, final RecyclerView recycler) throws NullPointerException{
        timer_treanding.schedule(new TimerTask() {
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
                Log.d("MyTag","::::::firstvisible::::: "+firstVisiblePosition+"last::::: "+lastVisiblePosition);

                if(list_of_projects.size()!=0) {
                    for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                        final Calendar cal = Calendar.getInstance();
                        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                        final long[] difference = new long[1];
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
                                        difference[0] = currentdate.getTime() - creation.getTime() - 10796000;
                                    } else if (currentdate.getTime() - creation.getTime() < difference[0]) {
                                        difference[0] = currentdate.getTime() - creation.getTime() - 10796000;
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
                                if (list_of_projects.get(finalI).isMoneySignFirst() && ((recycler.findViewHolderForAdapterPosition(finalI)) != null)&& editresponse.size() > Integer.parseInt(list_of_projects.get(finalI).getNumberofbidsandlastupdate().substring(0, list_of_projects.get(finalI).getNumberofbidsandlastupdate().indexOf(" ")))) {
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

                            @Override
                            public void onFailure(@NonNull Call<List<Bid_Object>> call0, @NonNull Throwable t) {
                            }
                        });
                    }

                }
            }
        }, 0, 1000);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("MyTag","search text");
        filter_trending(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        //adapter.filter(text);
        return false;
    }

}
