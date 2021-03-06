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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import Models.Compact_Project_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.ProjectObject;
import RetrofitModels.Tag_Object;
import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.AddBidActivity;
import honeybadgersapp.honeybadgers.Activities.BiddersActivity;
import honeybadgersapp.honeybadgers.Activities.ChatActivity;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.Activities.Show_Profile;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Notifications_adapter extends RecyclerView.Adapter<Dashboard_Notifications_adapter.MyViewHolder> implements Filterable{


    private Dialog myDialog;
    private Context mContext;
    private List<Compact_Project_Object> notification_cards;
    private List<Compact_Project_Object> notification_cards_filtered;
    private Timer timer = new Timer();
    //private int lastAnimatedPosition = -1;

    public Dashboard_Notifications_adapter(Context mContext, List<Compact_Project_Object> notification_cards) {
        this.mContext = mContext;
        this.notification_cards = notification_cards;

        //setHasStableIds(true);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
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
                            final ProjectObject editResponse = response.body();

                            if (response.isSuccessful()) {
                                Date currentTime = Calendar.getInstance().getTime();
                                final RecyclerView_tags_adapter recyclerAdapter;
                                TextView projecttitle = myDialog.findViewById(R.id.project_dialog_project_name);
                                final TextView projectid=myDialog.findViewById(R.id.project_dialog_project_id_edit);
                                TextView bid=myDialog.findViewById(R.id.project_dialog_bids_edit);
                                TextView budget=myDialog.findViewById(R.id.project_dialog_budget_edit);
                                TextView remainingtime=myDialog.findViewById(R.id.project_dialog_remaining_edit);
                                TextView description=myDialog.findViewById(R.id.project_dialog_project_descripton_edit);
                                Button bidbutton = myDialog.findViewById(R.id.project_dialog_bid_button);
                                ImageView map_button = myDialog.findViewById(R.id.project_dialog_maps_icon);
                                if((editResponse.getLongitude()!=null&&editResponse.getLatitude()!=null)){
                                    map_button.setVisibility(View.VISIBLE);
                                    map_button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final Dialog dialog = new Dialog(mContext);

                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                            dialog.setContentView(R.layout.dialogmap);////your custom content
                                            MapView mMapView = dialog.findViewById(R.id.mapView);
                                            MapsInitializer.initialize(mContext);

                                            mMapView.onCreate(dialog.onSaveInstanceState());
                                            mMapView.onResume();

                                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                                @Override
                                                public void onMapReady(final GoogleMap googleMap) {
                                                    LatLng posisiabsen = new LatLng(editResponse.getLatitude(), editResponse.getLongitude());
                                                    googleMap.addMarker(new MarkerOptions()
                                                            .position(
                                                                    posisiabsen)
                                                            .draggable(true).visible(true));
                                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiabsen,15));
                                                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                                                    //googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), null);
                                                }
                                            });
                                            dialog.show();

                                        }
                                    });
                                }
                                Button see_bidders_button= myDialog.findViewById(R.id.project_dialog_see_bidders_button);
                                see_bidders_button.setVisibility( ((LoginActivity.getCREDENTIALS()[3].equals("Client")&&editResponse.getUserId()==Integer.parseInt(LoginActivity.getCREDENTIALS()[4]))?View.VISIBLE:View.INVISIBLE) );
                                see_bidders_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Intent intent = new Intent(mContext, BiddersActivity.class);
                                        intent.putExtra("project_id", projectid.getText().toString());
                                        mContext.startActivity(intent);
                                    }
                                });
                                bidbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Intent intent = new Intent(mContext, AddBidActivity.class);
                                        intent.putExtra("project_id", projectid.getText().toString());
                                        mContext.startActivity(intent);
                                    }
                                });
                                final TextView employer=myDialog.findViewById(R.id.project_dialog_employer_name_edit);
                                final RatingBar ratingBar  = myDialog.findViewById(R.id.project_dialog_rating_bar);
                                RecyclerView skills=myDialog.findViewById(R.id.project_dialog_tags);
                                final int user_id= editResponse.getUserId();
                                final String[] user_email = new String[1];

                                //Get user name from id
                                Call<User> call2 = RetrofitClient.getInstance().getApi().getUserEmail(editResponse.getUserId());
                                call2.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(@NonNull Call<User> call2, @NonNull Response<User> response2) {
                                        User editResponse2 = response2.body();
                                        if (response2.isSuccessful()) {
                                            employer.setText(editResponse2.getName());
                                            user_email[0] =editResponse2.getEmail();

                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<User> call2, Throwable t) {
                                    }
                                });
                                Call<List<ProfileObject>> call4 = RetrofitClient.getInstance().getApi().clientProfileGet("token "+LoginActivity.getCREDENTIALS()[0],""+editResponse.getUserId());
                                call4.enqueue(new Callback<List<ProfileObject>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<List<ProfileObject>> call4, @NonNull Response<List<ProfileObject>> response2) {
                                        List<ProfileObject> editResponse2 = response2.body();
                                        if (response2.isSuccessful()) {
                                            assert editResponse2 != null;
                                            //ratingBar.setRating(editResponse2.get(0).getRating());
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<List<ProfileObject>> call4, Throwable t) {
                                    }
                                });

                                employer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try{
                                            if (!user_email[0].equals(LoginActivity.getCREDENTIALS()[1])) {
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
                                                        Show_Profile.user_email = "" + user_email[0];
                                                        Show_Profile.user_id = "" + user_id;
                                                        Intent i = new Intent(mContext, Show_Profile.class);
                                                        i.putExtra("role","client");
                                                        mContext.startActivity(i);
                                                    }
                                                });
                                                send_message.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        Firebase firebase_register;
                                                        firebase_register = new Firebase("https://honeybadgers-12976.firebaseio.com/Conversations").child(LoginActivity.getCREDENTIALS()[4]);
                                                        firebase_register.child(""+user_id).setValue(user_email[0]);
                                                        Log.d("MyTag","conersation email: "+user_email[0]);

                                                        ChatActivity.from_user = "" + LoginActivity.getCREDENTIALS()[4];
                                                        ChatActivity.to_user = "" +user_id ;
                                                        Intent i = new Intent(mContext, ChatActivity.class);
                                                        mContext.startActivity(i);
                                                    }
                                                });
                                                myDialog2.show();
                                            }
                                        }catch (NullPointerException e){

                                        }
                                    }
                                });
                                projecttitle.setText(editResponse.getTitle());
                                projectid.setText(""+editResponse.getId());
                                bid.setText(vHolder.updateinfo.getText().toString().substring(0,vHolder.updateinfo.getText().toString().indexOf(" ")));
                                budget.setText(new StringBuilder().append(editResponse.getBudgetMin()).append("-").append(editResponse.getBudgetMax()).toString());

                                Calendar cal = Calendar.getInstance();
                                Date currentdate=cal.getTime();
                                Date deadline = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                try {
                                    deadline = format.parse(editResponse.getDeadline());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long difference = deadline.getTime()-currentdate.getTime();


                                long days =  ((difference / (1000*60*60*24)));
                                long hours   =  (((difference-1000*60*60*24*days)/ (1000*60*60)));
                                Log.d("MyTag",currentdate+"_"+difference+"_"+hours+"_"+days+"_"+editResponse.getDeadline()+"____"+deadline);
                                if(days>=0 && hours >=0){
                                    remainingtime.setText(days+" Days and "+hours+" Hours");
                                }else{
                                    remainingtime.setText("OVER");
                                }

                                description.setText(editResponse.getDescription());
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                                final ArrayList <Tag_Object> t = new ArrayList<>();
                                recyclerAdapter = new RecyclerView_tags_adapter(mContext,t);

                                for (int i = 0; i < editResponse.getTags().length; i++) {

                                    Call<Tag_Object> call3 = RetrofitClient.getInstance().getApi().getTag(editResponse.getTags()[i]);
                                    final int finalI = i;
                                    call3.enqueue(new Callback<Tag_Object>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Tag_Object> call3, @NonNull Response<Tag_Object> response3) {
                                            Tag_Object editResponse3 = response3.body();
                                            if (response3.isSuccessful()) {
                                                Log.d("MyTag", "successful tag fetch id:" + editResponse3.getId());
                                                t.add(response3.body());
                                                recyclerAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        @Override
                                        public void onFailure(@NonNull Call<Tag_Object> call3, Throwable t) { }
                                    });
                                }
                                bidbutton.setVisibility( ((LoginActivity.getCREDENTIALS()[3].equals("Freelancer")&&!remainingtime.getText().toString().equalsIgnoreCase("OVER")&&editResponse.getUserId()!=Integer.parseInt(LoginActivity.getCREDENTIALS()[4]))?View.VISIBLE:View.INVISIBLE) );
                                skills.setLayoutManager(horizontalLayoutManager);
                                skills.setAdapter(recyclerAdapter);
                                recyclerAdapter.notifyDataSetChanged();
                                skills.setItemAnimator(new DefaultItemAnimator());
                                myDialog.show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<ProjectObject> call, Throwable t) {
                        }
                    });

            }
        });
        return  vHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
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


    public  void onBindViewHolder(@NonNull MyViewHolder holder, final int position){
//        holder.current_progressbar.setProgress(project_cards.get(position).getList_current());
// get name description and tags
//        switch (project_cards.get(position).getProduct_type()){
//            case "deneme":
//            //    holder.product_button.setBackgroundResource(R.drawable.deneme);
//                break;
//
//        }

        holder.setIsRecyclable(false);
        ((MyViewHolder) holder).bindData(notification_cards.get(position));
       // Glide.with(mContext);

      /*  if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            holder.container.setAnimation(animation);
            animation.start();
        }*/

    }

    @Override
    public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        notification_cards_filtered = notification_cards;
                    } else {
                        List<Compact_Project_Object> filteredList = new ArrayList<>();
                        for (Compact_Project_Object row : notification_cards) {

                            // name match condition. this might differ depending on your requirement

                            if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getTags().contains(charSequence)) {
                                filteredList.add(row);
                            }
                        }

                        notification_cards_filtered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = notification_cards_filtered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    notification_cards_filtered = (ArrayList<Compact_Project_Object>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private int project_id;
        private TextView project_name;
        private TextView project_highestbid;
        private TextView updateinfo;
        private RecyclerView recyclerViewTags;
        private ImageView money_sign;
        private ImageView approved_sign;
        protected View container;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            project_name = itemView.findViewById(R.id.compact_project_object_name);
            project_highestbid = itemView.findViewById(R.id.compact_project_given_bid);
            updateinfo = itemView.findViewById(R.id.compact_project_bids_time);
            recyclerViewTags=  itemView.findViewById(R.id.compact_project_object_tags_recyclerview);
            money_sign = itemView.findViewById(R.id.compact_project_money_sign);
            approved_sign= itemView.findViewById(R.id.verified_image);

        }

        public int getId() {
            return project_id;
        }

        public void setId(int id) {
            this.project_id = id;
        }


          void bindData( Compact_Project_Object viewModel){
            project_id=viewModel.getId();
            project_name.setText(viewModel.getName());
            if(viewModel.getTags()!=null){
                project_highestbid.setText(new StringBuilder().append("").append(viewModel.getHighestbid()).toString());
                updateinfo.setText(viewModel.getNumberofbidsandlastupdate());
                RecyclerView_tags_adapter recyclerAdapter = new RecyclerView_tags_adapter(mContext, viewModel.getTags());
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                recyclerViewTags.setLayoutManager(horizontalLayoutManager);
                recyclerViewTags.setAdapter(recyclerAdapter);
            }
            if(viewModel.isApproved()){
                approved_sign.setVisibility(View.VISIBLE);
                updateinfo.setVisibility(View.GONE);
                project_highestbid.setVisibility(View.GONE);
            }
        }


        public TextView getUpdateinfo() {
            return updateinfo;
        }

        public ImageView getMoney_sign() {
            return money_sign;
        }


        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }



}
