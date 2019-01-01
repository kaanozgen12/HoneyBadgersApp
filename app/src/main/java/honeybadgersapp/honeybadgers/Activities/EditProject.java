package honeybadgersapp.honeybadgers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.List;

import Adapters.ViewPagerAdapter;
import Fragments.Milestones_fragment;
import Fragments.MyProjects_project_description_Fragment;
import Fragments.ProjectProgressFragment;
import Fragments.Search_page_tab_fragment;
import RetrofitModels.Accepted_Milestone;
import RetrofitModels.Accepted_Project;
import RetrofitModels.Milestone_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProject extends AppCompatActivity {

    private int accepted_project_id;
    private boolean isVerified;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    private Milestones_fragment milestones_fragment =new Milestones_fragment();
    private MyProjects_project_description_Fragment description_fragment =new MyProjects_project_description_Fragment();
    private ProjectProgressFragment progress_fragment =new ProjectProgressFragment();
    private  Search_page_tab_fragment trending_search= new Search_page_tab_fragment();
    private  Search_page_tab_fragment recommended_search= new Search_page_tab_fragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition (0,0);
        super.onCreate(savedInstanceState);
        accepted_project_id= Integer.parseInt(getIntent().getExtras().getString("project_id"));
        isVerified = (getIntent().getExtras().getString("isVerified").equalsIgnoreCase("verified")?true:false);

        if(isVerified) {
            setContentView(R.layout.edit_project);

            tabLayout = findViewById(R.id.edit_project_page_tablayout_id);
            viewPager = findViewById(R.id.edit_project_page_viewpager);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(description_fragment, "Description");
            adapter.addFragment(milestones_fragment, "Milestones");
            adapter.addFragment(progress_fragment, "Progress");
            adapter.addFragment(recommended_search, "Files");
            adapter.addFragment(trending_search, "Invoices");
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(4);
            tabLayout.setupWithViewPager(viewPager, false);
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
            Call<Accepted_Project> call = RetrofitClient.getInstance().getApi().getAcceptedProjectById(accepted_project_id);
            call.enqueue(new Callback<Accepted_Project>() {
                @Override
                public void onResponse(Call<Accepted_Project> call, Response<Accepted_Project> response) {
                    Accepted_Project editResponse = response.body();
                    if (response.isSuccessful()) {
                        ((MyProjects_project_description_Fragment)adapter.getItem(0)).description.setText(editResponse.description);

                        Call<List<Accepted_Milestone>> call2 = RetrofitClient.getInstance().getApi().getAcceptedMilestonesById(accepted_project_id);
                        call2.enqueue(new Callback<List<Accepted_Milestone>>() {
                            @Override
                            public void onResponse(Call<List<Accepted_Milestone>> call2, Response<List<Accepted_Milestone>> response2) {
                                List<Accepted_Milestone> editResponse2 = response2.body();
                                Log.d("MyTag","success to get verified milestones: ");
                                if (response2.isSuccessful()) {
                                    for (int i=0; i<editResponse2.size(); i++){
                                        Log.d("MyTag","verified milestones size: "+editResponse2.size());
                                        ((Milestones_fragment)adapter.getItem(1)) .verified_milestones.add(new Milestone_Object(editResponse2.get(i).id,editResponse2.get(i).userId,editResponse2.get(i).acceptedprojectId,editResponse2.get(i).description,editResponse2.get(i).amount,editResponse2.get(i).createdAt,editResponse2.get(i).updatedAt,editResponse2.get(i).deadline,((editResponse2.get(i).isDone)?"complete":"verified") ) );
                                        ((Milestones_fragment)adapter.getItem(1)).recyclerAdapter_verified.notifyDataSetChanged();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Accepted_Milestone>> call2, Throwable t) {
                                Log.d("MyTag","failed to get verified milestones: "+t.getMessage());
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<Accepted_Project> call, Throwable t) {
                    Log.d("MyTag","failed to get verified milestones: "+t.getMessage());
                }


            });

        }else{
            Intent i = new Intent(EditProject.this, CreateProject.class);
            i.putExtra("project_id",""+accepted_project_id);
            startActivity(i);
        }
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
