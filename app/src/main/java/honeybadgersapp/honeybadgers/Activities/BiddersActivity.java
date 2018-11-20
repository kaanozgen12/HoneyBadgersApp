package honeybadgersapp.honeybadgers.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.List;

import Adapters.ViewPagerAdapter;
import Fragments.BidderInfoFragment;
import RetrofitModels.Bid_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiddersActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_bidder);
        tabLayout = findViewById(R.id.choose_bidder_tablayout_id);
        viewPager = findViewById(R.id.choose_bidder_viewpager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Call<List<Bid_Object>> call2 = RetrofitClient.getInstance().getApi().getAllBidsOfProject("token " + LoginActivity.getCREDENTIALS()[0],getIntent().getIntExtra("project_id",-1));
        call2.enqueue(new Callback<List<Bid_Object>>() {
            @Override
            public void onResponse(Call<List<Bid_Object>> call2, Response<List<Bid_Object>> response) {
                for (int i= 0 ;i< response.body().size(); i++){
                    adapter.addFragment(new BidderInfoFragment(response.body().get(i).getUserId(),response.body().get(i).getId(),response.body().get(i).getDescription()),""+i);
                    adapter.notifyDataSetChanged();

                }
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(response.body().size());
                tabLayout.setupWithViewPager(viewPager,false);
            }
            @Override
            public void onFailure(Call<List<Bid_Object>> call2, Throwable t) {
            }
        });


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
