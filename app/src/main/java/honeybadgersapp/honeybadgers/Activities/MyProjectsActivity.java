package honeybadgersapp.honeybadgers.Activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import Adapters.Dashboard_Notifications_adapter;
import Adapters.RecyclerItemTouchHelper;
import Models.Compact_Project_Object;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProjectsActivity extends AppCompatActivity {
    public List<Compact_Project_Object> listOfProjects =new ArrayList<>();
    private RelativeLayout relativeLayout;
    RecyclerView mRecylerView;
    Dashboard_Notifications_adapter recyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_projects);

        relativeLayout = findViewById(R.id.my_projects_activity);
        mRecylerView= findViewById(R.id.my_projects_recyclerview);
        recyclerAdapter =new Dashboard_Notifications_adapter(this,listOfProjects);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setAdapter(recyclerAdapter);


        Log.d("MyTag", "id :"+LoginActivity.getCREDENTIALS()[4]);
        Call<List<ProjectObject>> call = RetrofitClient.getInstance().getApi().getMyProjects(Integer.parseInt(LoginActivity.getCREDENTIALS()[4]));
        call.enqueue(new Callback<List<ProjectObject>>() {

            @Override
            public  void onResponse(@NonNull Call<List<ProjectObject>> call, @NonNull Response<List<ProjectObject>> response) {
                List<ProjectObject> editResponse = response.body();
                Log.d("MyTag", "is response successful "+response.isSuccessful());
                if (response.isSuccessful()) {

                    Log.d("MyTag", "size response :"+response.body().size());
                    for (int i = 0; i < editResponse.size(); i++) {
                        Log.d("MyTag", "iiiiii "+editResponse.get(i).getId());
                        final ArrayList <Tag_Object> t = new ArrayList<>();
                        final Compact_Project_Object temp = new Compact_Project_Object(editResponse.get(i).getId(),editResponse.get(i).getTitle(),0,"0",t);
                        listOfProjects.add(temp);
                        recyclerAdapter.notifyDataSetChanged();
                        Log.d("MyTag", "listofproject size  :"+listOfProjects.size());
                        for (int j =0 ; j<editResponse.get(i).getTags().length;j++){
                            Call<Tag_Object> call2 = RetrofitClient.getInstance().getApi().getTag(editResponse.get(i).getTags()[j]);
                            call2.enqueue(new Callback<Tag_Object>() {
                                @Override
                                public  void onResponse(@NonNull Call<Tag_Object> call2, @NonNull Response<Tag_Object> response2) {
                                    Tag_Object editResponse2 = response2.body();
                                    if (response2.isSuccessful()) {
                                        t.add(editResponse2);
                                        recyclerAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public  void onFailure(Call<Tag_Object> call2, Throwable t) {
                                }
                            });

                        }

                    }
                }
            }
            @Override
            public  void onFailure(Call<List<ProjectObject>> call, Throwable t) {
                Log.d("MyTag", "Search failed");
            }
        });

       // recyclerAdapter.notifyDataSetChanged();
        SwipeControllerActions itemTouchHelperCallback =new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                new android.support.v7.app.AlertDialog.Builder(MyProjectsActivity.this)
                        .setTitle("DELETE?")
                        .setMessage("Are you sure you want to delete your project? Once you delete you will not be able to recover your project and all bidders will be notified" )
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Call<Void> call2 = RetrofitClient.getInstance().getApi().deleteProject("token "+LoginActivity.getCREDENTIALS()[0],listOfProjects.get(position).getId(),true);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public  void onResponse(@NonNull Call<Void> call2, @NonNull Response<Void> response2) {
                                        // remove the item from recycler view
                                        recyclerAdapter.removeItem(position);
                                    }
                                    @Override
                                    public  void onFailure(Call<Void> call2, Throwable t) {
                                        Log.d("MyTag","proje silme başarısız");
                                    }
                                });
                            }
                        }).create().show();
            }
        };
        final RecyclerItemTouchHelper swipeController = new RecyclerItemTouchHelper(itemTouchHelperCallback);
        mRecylerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecylerView);

    }
//

    //
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("MyTag", "size listofprojects :"+listOfProjects.size());
    }

    public abstract static class SwipeControllerActions {

        public void onLeftClicked(int position) {}

        public void onRightClicked(int position) {}

    }
}
