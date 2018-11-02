package Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Models.Compact_Project_Object;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.ChatActivity;
import honeybadgersapp.honeybadgers.Activities.Show_Profile;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Notifications_adapter extends RecyclerView.Adapter<Dashboard_Notifications_adapter.MyViewHolder>{



    private Dialog myDialog;
    private Context mContext;
    private List<Compact_Project_Object> notification_cards;
    //private int lastAnimatedPosition = -1;

    public Dashboard_Notifications_adapter(Context mContext, List<Compact_Project_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;
        //setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType){

        final View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        //v = LayoutInflater.from(mContext).inflate(R.layout.compact_project_object,parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Test click"+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();

                myDialog = new Dialog(mContext);
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(R.layout.project_dialog);
                myDialog.setCancelable(true);
                myDialog.setCanceledOnTouchOutside(true);

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
                            final RecyclerView_tags_adapter recyclerAdapter;
                            TextView projecttitle = myDialog.findViewById(R.id.project_dialog_project_name);
                            TextView projectid=myDialog.findViewById(R.id.project_dialog_project_id_edit);
                            TextView bid=myDialog.findViewById(R.id.project_dialog_bids_edit);
                            TextView budget=myDialog.findViewById(R.id.project_dialog_budget_edit);
                            TextView remainingtime=myDialog.findViewById(R.id.project_dialog_remaining_edit);
                            TextView description=myDialog.findViewById(R.id.project_dialog_project_descripton_edit);
                            final TextView employer=myDialog.findViewById(R.id.project_dialog_employer_name_edit);
                            RecyclerView skills=myDialog.findViewById(R.id.project_dialog_tags);
                            final int user_id= editResponse.getUserId();

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
                            employer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!employer.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                        Dialog myDialog2 = new Dialog(mContext);
                                        myDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        myDialog2.setContentView(R.layout.username_dialog);
                                        myDialog2.setCancelable(true);
                                        myDialog2.setCanceledOnTouchOutside(true);

                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                        lp.copyFrom(myDialog2.getWindow().getAttributes());
                                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                        lp.gravity = Gravity.CENTER;
                                        myDialog2.getWindow().setAttributes(lp);
                                        TextView see_profile = myDialog2.findViewById(R.id.see_profile);
                                        TextView send_message = myDialog2.findViewById(R.id.send_message);
                                        see_profile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Show_Profile.user_email = "" + employer.getText();
                                                Show_Profile.user_id = "" + user_id;
                                                Intent i = new Intent(mContext, Show_Profile.class);
                                                mContext.startActivity(i);
                                            }
                                        });
                                        send_message.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Firebase.setAndroidContext(mContext);
                                                Firebase firebase_register;
                                                firebase_register = new Firebase("https://honeybadgers-12976.firebaseio.com/Conversations/" + FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().hashCode());
                                                firebase_register
                                                        .push()
                                                        .setValue(employer.getText().toString());
                                                ChatActivity.from_user = "" + FirebaseAuth.getInstance().getCurrentUser().getEmail().hashCode();
                                                ChatActivity.to_user = "" + employer.getText().toString().hashCode();
                                                Intent i = new Intent(mContext, ChatActivity.class);
                                                mContext.startActivity(i);
                                            }
                                        });
                                        myDialog2.show();
                                    }
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
                            recyclerAdapter = new RecyclerView_tags_adapter(mContext,t);

                            for (int i = 0; i < editResponse.getTags().length; i++) {

                                Call<Tag_Object> call3 = RetrofitClient.getInstance().getApi().getTag(editResponse.getTags()[i]);
                                final int finalI = i;
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
                            skills.setLayoutManager(horizontalLayoutManager);
                            skills.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();
                            skills.setItemAnimator(new DefaultItemAnimator());
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


    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return notification_cards.size();
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.compact_project_object;
    }

    public void removeItem(int position) {
        notification_cards.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Compact_Project_Object item, int position) {
        notification_cards.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    /* @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        holder.clearAnimation();
    }*/


    public synchronized void onBindViewHolder(@NonNull MyViewHolder holder, final int position){
//        holder.current_progressbar.setProgress(project_cards.get(position).getList_current());
// get name description and tags
//        switch (project_cards.get(position).getProduct_type()){
//            case "deneme":
//            //    holder.product_button.setBackgroundResource(R.drawable.deneme);
//                break;
//
//        }

        ((MyViewHolder) holder).bindData(notification_cards.get(position));
        Glide.with(mContext);

      /*  if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            holder.container.setAnimation(animation);
            animation.start();
        }*/

    }





    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int project_id;
        private TextView project_name;
        private TextView project_highestbid;
        private TextView updateinfo;
        private RecyclerView recyclerViewTags;
        protected View container;
        public RelativeLayout viewBackground, viewForeground, viewBaseground;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            project_name = itemView.findViewById(R.id.compact_project_object_name);
            project_highestbid = itemView.findViewById(R.id.compact_project_given_bid);
            updateinfo = itemView.findViewById(R.id.compact_project_bids_time);
            recyclerViewTags=  itemView.findViewById(R.id.compact_project_object_tags_recyclerview);
            viewBackground = container.findViewById(R.id.view_background);
            viewForeground = container.findViewById(R.id.view_foreground);
            viewBaseground = container.findViewById(R.id.view_backbackground);

        }

        public int getId() {
            return project_id;
        }

        public void setId(int id) {
            this.project_id = id;
        }

        public  void bindData(final Compact_Project_Object viewModel){
            project_id=viewModel.getId();
            project_name.setText(viewModel.getName());
            project_highestbid.setText(new StringBuilder().append("").append(viewModel.getHighestbid()).toString());
            updateinfo.setText(viewModel.getNumberofbidsandlastupdate());


            RecyclerView_tags_adapter recyclerAdapter = new RecyclerView_tags_adapter(mContext, viewModel.getTags());
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewTags.setLayoutManager(horizontalLayoutManager);
            recyclerViewTags.setAdapter(recyclerAdapter);

        }



        public void clearAnimation(){
            container.clearAnimation();
        }


    }


}
