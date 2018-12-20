package honeybadgersapp.honeybadgers.Activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.Comment_adapter;
import RetrofitModels.Comment_Object;
import RetrofitModels.ProfileObject;
import RetrofitModels.User;
import api.RetrofitClient;
import honeybadgersapp.honeybadgers.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Show_Profile extends AppCompatActivity {

    public static String user_id;
    public static String user_email;
    // UI references.
    private TextView mName;
    private TextView mBio;
    private TextView mEmail;
    private ImageView mPhoto;
    private RatingBar mRating;
    private Button mChangeProfile;
    private Button mComment;
    private TextView role_info_text;
    public RecyclerView myrecyclerview;
    public List<Comment_Object> list_of_comments =new ArrayList<>();
    public Comment_adapter recyclerAdapter;
    public String role="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition (0,0);
        setContentView(R.layout.see_profile);
        mName =  findViewById(R.id.show_profile_name);
        role_info_text = findViewById(R.id.show_profile_rating);
        mBio=findViewById(R.id.show_profile_description_edit);
        mEmail=findViewById(R.id.show_profile_email);
        mPhoto=findViewById(R.id.show_profile_photo);
        mComment= findViewById(R.id.show_profile_comment_button);
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog myDialog = new Dialog(Show_Profile.this);
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(R.layout.comment_adder);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(myDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                myDialog.getWindow().setAttributes(lp);
                Button save_button = myDialog.findViewById(R.id.comment_adder_save_button);
                final EditText description = myDialog.findViewById(R.id.comment_adder_description);
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<Void> call;

                        if(role.equalsIgnoreCase("freelancer")){
                            call = RetrofitClient.getInstance().getApi().post_freelancer_comment("token "+LoginActivity.getCREDENTIALS()[0],Integer.parseInt(user_id),description.getText().toString());
                        }else{
                            call  = RetrofitClient.getInstance().getApi().post_client_comment("token "+LoginActivity.getCREDENTIALS()[0],Integer.parseInt(user_id),description.getText().toString());
                        }
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response3) {
                                if (response3.isSuccessful()) {
                                    Log.d("MyTag","Successful comment commit");
                                    myDialog.dismiss();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });

                    }
                });
                myDialog.show();
            }
        });
        mRating= findViewById(R.id.show_profile_rating_bar);
        mChangeProfile = findViewById(R.id.see_profile_change_profile);
        myrecyclerview =  findViewById(R.id.show_profile_comments_recyclerview);
        recyclerAdapter =new Comment_adapter(getBaseContext(),list_of_comments);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        myrecyclerview.setAdapter(recyclerAdapter);
        role= getIntent().getStringExtra("role");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Call<List<ProfileObject>> call;

        if(role.equalsIgnoreCase("freelancer")){
            call = RetrofitClient.getInstance().getApi().freelancerProfileGet("token "+LoginActivity.getCREDENTIALS()[0],user_id);
            role_info_text.setText("Freelancer Rating");
        }else{
            call  = RetrofitClient.getInstance().getApi().clientProfileGet("token "+LoginActivity.getCREDENTIALS()[0],user_id);
            role_info_text.setText("Client Rating");
        }
        call.enqueue(new Callback<List<ProfileObject>>() {
            @Override
            public void onResponse(Call<List<ProfileObject>> call, Response<List<ProfileObject>> response) {
                final List<ProfileObject> editResponse = response.body();
                if (response.isSuccessful()){
                    new SendHttpRequestTask().execute(editResponse.get(0).getAvatar());
                    mBio.setText( editResponse.get(0).getBody());
                    mEmail.setText(user_email);
                    Call<List<User>> call2 = RetrofitClient.getInstance().getApi().user_id(user_email);
                    call2.enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call2, Response<List<User>> response2) {
                            User editResponse2 = response2.body().get(0);
                            if (response2.isSuccessful()) {
                               mName.setText(editResponse2.getUsername());
                                Call<List<Comment_Object>> call3 ;
                                if(role.equalsIgnoreCase("freelancer")){
                                    call3 = RetrofitClient.getInstance().getApi().freelancer_comment(editResponse2.getId());
                                }else{
                                    call3 = RetrofitClient.getInstance().getApi().client_comment(editResponse2.getId());
                                }
                                call3.enqueue(new Callback<List<Comment_Object>>() {
                                    @Override
                                    public void onResponse(Call<List<Comment_Object>> call3, Response<List<Comment_Object>> response3) {
                                        if (response3.isSuccessful()) {
                                            for (int i= 0 ; i<response3.body().size(); i++){
                                                list_of_comments.add(new Comment_Object(response3.body().get(i).id,response3.body().get(i).userId,response3.body().get(i).profileId,response3.body().get(i).description,response3.body().get(i).createdAt,response3.body().get(i).updatedAt,response3.body().get(i).commenterAvatar));
                                                recyclerAdapter.notifyItemInserted(list_of_comments.size()-1);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<List<Comment_Object>> call3, Throwable t) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<List<User>> call2, Throwable t) {
                            }
                    });



                    //mPhoto.setBackground();
                    //mRating.setRating(editResponse.get(0).getRating());
                }
            }

            @Override
            public void onFailure(Call<List<ProfileObject>> call, Throwable t) {
                Log.d("MyTag","Failed");
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


