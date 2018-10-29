package Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Models.Compact_Project_Object;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewDashboard_Notifications extends RecyclerView.Adapter<RecyclerViewDashboard_Notifications.MyViewHolder>{



    Dialog myDialog;
    Context mContext;
    List<Compact_Project_Object> notification_cards;
    private int lastAnimatedPosition = -1;

    public RecyclerViewDashboard_Notifications(Context mContext, List<Compact_Project_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.compact_project_object,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Test click"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();

                myDialog = new Dialog(mContext);
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(R.layout.project_dialog);
                myDialog.setCancelable(false);
                myDialog.setCanceledOnTouchOutside(false);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(myDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                myDialog.getWindow().setAttributes(lp);

               /* Window window = myDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);*/


                Call<ProjectObject> call = RetrofitClient.getInstance().getApi().getProjectbyId(vHolder.getId());
                call.enqueue(new Callback<ProjectObject>() {
                    @Override
                    public void onResponse(Call<ProjectObject> call, final Response<ProjectObject> response) {
                        ProjectObject editResponse = response.body();

                        if (response.isSuccessful()) {
                            Date currentTime = Calendar.getInstance().getTime();
                            final RecyclerView_tags recyclerAdapter;
                            TextView projecttitle = myDialog.findViewById(R.id.project_dialog_project_name);
                            TextView projectid=myDialog.findViewById(R.id.project_dialog_project_id_edit);
                            TextView bid=myDialog.findViewById(R.id.project_dialog_bids_edit);
                            TextView budget=myDialog.findViewById(R.id.project_dialog_budget_edit);
                            TextView remainingtime=myDialog.findViewById(R.id.project_dialog_remaining_edit);
                            TextView description=myDialog.findViewById(R.id.project_dialog_project_descripton_edit);
                            final TextView employer=myDialog.findViewById(R.id.project_dialog_employer_name_edit);
                            RecyclerView skills=myDialog.findViewById(R.id.project_dialog_tags);

                            //Get user email from id
                            Call<User> call2 = RetrofitClient.getInstance().getApi().getUserEmail(editResponse.getUserId());
                            call2.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call2, Response<User> response2) {
                                    User editResponse2 = response2.body();
                                    if (response2.isSuccessful()) {
                                        employer.setText(editResponse2.getEmail());

                                    }
                                }
                                @Override
                                public void onFailure(Call<User> call2, Throwable t) {
                                }
                            });
                            projecttitle.setText(editResponse.getTitle());
                            projectid.setText(""+editResponse.getId());
                            bid.setText("0");
                            budget.setText(new StringBuilder().append(editResponse.getBudgetMin()).append("-").append(editResponse.getBudgetMax()).toString());
                            remainingtime.setText("Not Handled");
                            description.setText(editResponse.getDescription());
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                            final ArrayList <Tag_Object> t = new ArrayList<>();
                            recyclerAdapter = new RecyclerView_tags(mContext,t);

                            for (int i = 0; i < editResponse.getTags().length; i++) {

                                Call<Tag_Object> call3 = RetrofitClient.getInstance().getApi().getTag(editResponse.getTags()[i]);
                                call3.enqueue(new Callback<Tag_Object>() {
                                    @Override
                                    public void onResponse(Call<Tag_Object> call3, Response<Tag_Object> response3) {
                                        Tag_Object editResponse3 = response3.body();
                                        if (response3.isSuccessful()) {
                                            Log.d("MyTag", "successful tag fetch id:" + editResponse3.getId());
                                            t.add(response3.body());
                                            recyclerAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Tag_Object> call3, Throwable t) { }
                                });
                            }
                            Log.d("MyTag","number of tags : "+editResponse.getTags().length);
                            skills.setLayoutManager(horizontalLayoutManager);
                            skills.setAdapter(recyclerAdapter);
                            skills.setItemAnimator(new DefaultItemAnimator());
                            myDialog.setCancelable(true);
                            myDialog.setCanceledOnTouchOutside(true);
                            myDialog.show();



                        }
                    }
                    @Override
                    public void onFailure(Call<ProjectObject> call, Throwable t) {
                    }
                });


            }
        });
        return  vHolder;
    }




    public synchronized void onBindViewHolder(MyViewHolder holder, int position){
//        holder.current_progressbar.setProgress(project_cards.get(position).getList_current());
// get name description and tags
//        switch (project_cards.get(position).getProduct_type()){
//            case "deneme":
//            //    holder.product_button.setBackgroundResource(R.drawable.deneme);
//                break;
//
//        }
        holder.project_id=notification_cards.get(position).getId();
        holder.project_name.setText(notification_cards.get(position).getName());
        holder.project_highestbid.setText("" + notification_cards.get(position).getHighestbid());
        holder.updateinfo.setText(notification_cards.get(position).getNumberofbidsandlastupdate());
        synchronized (this) {
            RecyclerView_tags recyclerAdapter = new RecyclerView_tags(mContext, notification_cards.get(position).getTags());
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerViewTags.setLayoutManager(horizontalLayoutManager);
            holder.recyclerViewTags.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            holder.container.setAnimation(animation);
            animation.start();
        }

        }
    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return notification_cards.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int project_id;
        private TextView project_name;
        private TextView project_highestbid;
        private TextView updateinfo;
        private RecyclerView recyclerViewTags;
        protected View container;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            project_name = itemView.findViewById(R.id.compact_project_object_name);
            project_highestbid = itemView.findViewById(R.id.compact_project_given_bid);
            updateinfo = itemView.findViewById(R.id.compact_project_bids_time);
            recyclerViewTags= itemView.findViewById(R.id.compact_project_object_tags_recyclerview);

        }

        public int getId() {
            return project_id;
        }

        public void setId(int id) {
            this.project_id = id;
        }

        public void clearAnimation(){
            container.clearAnimation();
        }


    }


}
