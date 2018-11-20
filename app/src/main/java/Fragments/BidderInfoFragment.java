package Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Milestones_adapter;
import RetrofitModels.Milestone_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.Activities.ChatActivity;
import honeybadgersapp.honeybadgers.Activities.LoginActivity;
import honeybadgersapp.honeybadgers.Activities.Show_Profile;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BidderInfoFragment extends Fragment {


    public  int user_id;
    public  int bid_id;
    public String description;
    // UI references.
    private TextView mName;
    private TextView mEmail;
    private TextView mDescription;
    private ImageView mPhoto;
    private RatingBar mRating;
    private Button mApprove;
    private TextView mTotalText;
    final String[] user_email = new String[1];
    View view;
    protected Boolean mVisibleToUserFlag = null;
    public RecyclerView myrecyclerview;
    public List<Milestone_Object> list_of_milestones = new ArrayList<>();
    public Milestones_adapter recyclerAdapter;

    public BidderInfoFragment() {

        }

    @SuppressLint("ValidFragment")
    public BidderInfoFragment(int user_id, int bid_id, String description) {
        this.user_id = user_id;
        this.bid_id = bid_id;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("MyTag", "Hello fragent created:!!!!!!!!!!!!!!!!!");
        view = inflater.inflate(R.layout.bidder_info_fragment, container, false);
        mName = view.findViewById(R.id.bidder_info_fragment_profile_name);
        mEmail = view.findViewById(R.id.bidder_info_fragment_profile_email);
        mPhoto = view.findViewById(R.id.bidder_info_fragment_profile_photo);
        mRating = view.findViewById(R.id.bidder_info_fragment_profile_rating_bar);
        mDescription = view.findViewById(R.id.bidder_info_fragment_profile_description_edit);
        mApprove = view.findViewById(R.id.bidder_info_fragment_approve_freelancer);
        mTotalText= view.findViewById(R.id.bidder_info_fragment_header_bottom_amount_text);
        myrecyclerview = view.findViewById(R.id.bidder_info_fragment_recyclerview);
       getBidder();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerAdapter = new Milestones_adapter(getContext(), list_of_milestones);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVisibleToUserFlag != null && mVisibleToUserFlag) {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && (mVisibleToUserFlag == null || mVisibleToUserFlag == false)) {
            mVisibleToUserFlag = true;
            if (isResumed()) {
                onVisible();
            }
        } else if (!isVisibleToUser && mVisibleToUserFlag == null) {
            mVisibleToUserFlag = false;
        } else if (!isVisibleToUser && mVisibleToUserFlag) {
            mVisibleToUserFlag = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mVisibleToUserFlag = null;
    }

    /**
     * This method invoked when page stand visible to user.
     */
    public void onVisible() {
        recyclerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void getBidder() {
            Call<List<ProfileObject>> call1 = RetrofitClient.getInstance().getApi().userProfileGet("token " + LoginActivity.getCREDENTIALS()[0],""+user_id);
            call1.enqueue(new Callback<List<ProfileObject>>() {
                @Override
                public void onResponse(Call<List<ProfileObject>> call1, Response<List<ProfileObject>> response) {
                    final List<ProfileObject> editResponse2 = response.body();
                    if (response.isSuccessful() && editResponse2.size() >= 1) {
                        Call<List<Milestone_Object>> call2 = RetrofitClient.getInstance().getApi().getAllMilestonesOfBid("token " + LoginActivity.getCREDENTIALS()[0], bid_id);
                        call2.enqueue(new Callback<List<Milestone_Object>>() {
                            @Override
                            public void onResponse(Call<List<Milestone_Object>> call2, Response<List<Milestone_Object>> response) {
                                int temp = 0 ;
                                for (int x = 0; x < response.body().size(); x++) {
                                    list_of_milestones.add(new Milestone_Object(response.body().get(x).getAmount(), response.body().get(x).getDescription(), response.body().get(x).getDeadline()));
                                    temp+=response.body().get(x).getAmount();
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                                //Get user name from id
                                Call<User> call3 = RetrofitClient.getInstance().getApi().getUserEmail(user_id);
                                call3.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(@NonNull Call<User> call3, @NonNull Response<User> response2) {
                                        User editResponse2 = response2.body();
                                        if (response2.isSuccessful()) {
                                            user_email[0] =editResponse2.getEmail();
                                            mEmail.setText(user_email[0]);
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<User> call3, Throwable t) {
                                    }
                                });

                                new SendHttpRequestTask().execute(editResponse2.get(0).getAvatar());
                                mName.setText(editResponse2.get(0).getName());
                                mName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                            Dialog myDialog2 = new Dialog(getContext());
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
                                                    Intent i = new Intent(getContext(), Show_Profile.class);
                                                    getContext().startActivity(i);
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
                                                    Intent i = new Intent(getContext(), ChatActivity.class);
                                                    getContext().startActivity(i);
                                                }
                                            });
                                            myDialog2.show();
                                    }
                                });

                                mRating.setRating(editResponse2.get(0).getRating());
                                mDescription.setText(description);
                                mTotalText.setText(""+temp);
                            }
                            @Override
                            public void onFailure(Call<List<Milestone_Object>> call2, Throwable t) {
                            }
                        });

                    }

                }

                @Override
                public void onFailure(Call<List<ProfileObject>> call1, Throwable t) {
                }
            });

        }

    public class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                //
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null)
                mPhoto.setImageBitmap(result);
        }
    }

}


